package zerobase.tableNow.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.user.dto.RegisterDto;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.domain.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public RegisterDto register(RegisterDto registerDto) {
        Optional<UsersEntity> optionalUsers = userRepository.findByUserId(registerDto.getUserId());
        if (optionalUsers.isPresent()){
            log.info("이미 존재하는 아이디 입니다.");
            throw new RuntimeException("이미 존재하는 아이디 입니다.");
        }

        Role userRole = registerDto.isManagerYn() ? Role.MANAGER : Role.USER;

        UsersEntity userEntity = UsersEntity.builder()
                .userId(registerDto.getUserId())
                .name(registerDto.getName())
                .password(registerDto.getPassword())
                .email(registerDto.getEmail())
                .phone(registerDto.getPhone())
                .emailAuthYn(registerDto.isEmailAuthYn())
                .emailAuthDt(registerDto.getEmailAuthDt())
                .emailAuthKey(UUID.randomUUID().toString())
                .role(userRole)
                .managerYn(registerDto.isManagerYn())
                .userStatus(Status.REQ)
                .build();
        UsersEntity savedEntity = userRepository.save(userEntity);

        // 저장된 Entity를 DTO로 변환하여 반환
        return RegisterDto.builder()
                .userId(savedEntity.getUserId())
                .name(savedEntity.getName())
                .password(savedEntity.getPassword())
                .email(savedEntity.getEmail())
                .phone(savedEntity.getPhone())
                .emailAuthYn(savedEntity.isEmailAuthYn())
                .emailAuthDt(savedEntity.getEmailAuthDt())
                .emailAuthKey(savedEntity.getEmailAuthKey())
                .role(savedEntity.getRole())
                .managerYn(savedEntity.isManagerYn())
                .userStatus(savedEntity.getUserStatus())
                .build();
    }

}
