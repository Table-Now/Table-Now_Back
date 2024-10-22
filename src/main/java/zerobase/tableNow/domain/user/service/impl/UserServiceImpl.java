package zerobase.tableNow.domain.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zerobase.tableNow.components.MailComponents;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.user.dto.RegisterDto;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.mapper.UserMapper;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.domain.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MailComponents mailComponents;
    @Override
    public RegisterDto register(RegisterDto registerDto) {
        // 중복 체크
        Optional<UsersEntity> optionalUsers = userRepository.findByUserId(registerDto.getUserId());
        if (optionalUsers.isPresent()) {
            log.info("이미 존재하는 아이디 입니다.");
            throw new RuntimeException("이미 존재하는 아이디 입니다.");
        }

        // DTO -> Entity 변환 및 저장
        UsersEntity userEntity = userMapper.toEntity(registerDto);
        UsersEntity savedEntity = userRepository.save(userEntity);

        // 인증 메일 발송
        String email = registerDto.getEmail();
        String subject = "TableNow 이메일 인증";
        String text = mailComponents.getEmailAuthTemplate(savedEntity.getUserId(), savedEntity.getEmailAuthKey());

        boolean sendResult = mailComponents.sendMail(email, subject, text);
        if (!sendResult) {
            log.error("회원가입 인증 메일 발송 실패");
        }

        return userMapper.toDto(savedEntity);
    }

    @Transactional
    public boolean emailAuth(String userId, String authKey) {
        Optional<UsersEntity> optionalUser = userRepository.findByUserIdAndEmailAuthKey(userId, authKey);
        if (optionalUser.isEmpty()) {
            return false;
        }

        UsersEntity user = optionalUser.get();
        if (user.isEmailAuthYn()) {
            return false;
        }

        user.setEmailAuthYn(true);
        user.setEmailAuthDt(LocalDateTime.now());
        user.setUserStatus(Status.ING);
        userRepository.save(user);

        return true;
    }

}
