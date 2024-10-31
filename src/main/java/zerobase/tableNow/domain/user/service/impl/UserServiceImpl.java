package zerobase.tableNow.domain.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.tableNow.components.MailComponents;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.token.TokenProvider;
import zerobase.tableNow.domain.user.dto.DeleteDto;
import zerobase.tableNow.domain.user.dto.LoginDto;
import zerobase.tableNow.domain.user.dto.RePasswordDto;
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
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param registerDto
     * @return 등록된 회원 정보 반환
     */
    @Override
    public RegisterDto register(RegisterDto registerDto) {
        // 중복 체크
        Optional<UsersEntity> optionalUsers = userRepository.findByUser(registerDto.getUser());
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
        String text = mailComponents.getEmailAuthTemplate(savedEntity.getUser(), savedEntity.getEmailAuthKey());

        boolean sendResult = mailComponents.sendMail(email, subject, text);
        if (!sendResult) {
            log.error("회원가입 인증 메일 발송 실패");
        }

        return userMapper.toDto(savedEntity);
    }

    /**
     * 이메일 인증
     * @param user
     * @param emailAuthKey
     * @return 인증 성공시 True, 인증 실패시 False
     */
    @Transactional
    public boolean emailAuth(String user, String emailAuthKey) {
        Optional<UsersEntity> optionalUser = userRepository.findByUserAndEmailAuthKey(user, emailAuthKey);
        if (optionalUser.isEmpty()) {
            return false;
        }

        UsersEntity users= optionalUser.get();
        if (users.isEmailAuthYn()) {
            return false;
        }

        users.setEmailAuthYn(true);
        users.setEmailAuthDt(LocalDateTime.now());
        users.setUserStatus(Status.ING);
        userRepository.save(users);

        return true;
    }

    /**
     * 로그인
     * @param loginDto
     * @return responseDto 반환
     */
    @Override
    public LoginDto login(LoginDto loginDto) {
        UsersEntity user = userRepository.findByUser(loginDto.getUser())
                .orElseThrow(() -> new RuntimeException("해당 ID가 없습니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        if (!user.isEmailAuthYn()) {
            throw new RuntimeException("이메일 인증을 완료해주세요.");
        }

        // 로그인 성공 시 JWT 토큰 생성
        String accessToken = tokenProvider.generateAccessToken(loginDto);

        LoginDto responseDto = userMapper.toLoginDto(user);

        responseDto.setToken(accessToken);

        return responseDto;
    }

    /**
     * 비밀번호 재설정
     * @param rePasswordDto
     * @return 성공시 "success" 반환
     */
    @Override
    public String rePassword(RePasswordDto rePasswordDto) {
            // 1. 사용자 확인 (userId로 유효성 검증)
            Optional<UsersEntity> optionalUser = userRepository.findByUser(rePasswordDto.getUser());

            // 유효성 검증: user가 존재하는지 확인
            UsersEntity user = optionalUser.orElseThrow(() -> new RuntimeException("해당 사용자 정보를 찾을 수 없습니다."));

            // 2. 이메일 검증
            if (!rePasswordDto.getEmail().equals(user.getEmail())) {
                throw new RuntimeException("이메일이 일치하지 않습니다.");
            }

            // 3. 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(rePasswordDto.getRePassword());

            // 4. 비밀번호 저장
            user.setPassword(encodedPassword);
            userRepository.save(user);

            return "Success";
    }

    /**
     * 회원 탈퇴
     * @param user
     * @return DeleteDto 반환
     */
    @Override
    public DeleteDto userDelete(String user) {
        Optional<UsersEntity> optionalUsers = userRepository.findByUser(user);
        if (optionalUsers.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UsersEntity users = optionalUsers.get();
        users.setUserStatus(Status.STOP);
        UsersEntity savedUser = userRepository.save(users);

        return new DeleteDto(savedUser.getUser(), savedUser.getRole());
    }
}