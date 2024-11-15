package zerobase.tableNow.domain.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.tableNow.components.MailComponents;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.token.TokenProvider;
import zerobase.tableNow.domain.user.dto.*;
import zerobase.tableNow.domain.user.entity.EmailEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.mapper.UserMapper;
import zerobase.tableNow.domain.user.repository.EmailRepository;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.domain.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final EmailRepository emailRepository;
    private final ReservationRepository reservationRepository;
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
            UsersEntity existingUser = optionalUsers.get();
            if (existingUser.getUserStatus() == Status.STOP) {
                log.info("회원탈퇴한 ID 입니다. 다른 ID를 사용해주세요.");
                throw new RuntimeException("회원탈퇴한 ID 입니다. 다른 ID를 사용해주세요.");
            } else {
                log.info("이미 존재하는 아이디 입니다.");
                throw new RuntimeException("이미 존재하는 아이디 입니다.");
            }
        }

        // DTO -> Entity 변환 및 저장
        UsersEntity userEntity = userMapper.toEntity(registerDto);
        UsersEntity savedEntity = userRepository.save(userEntity);

        // EmailEntity 저장 (이메일 인증 관련)
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setUser(userEntity);
        emailEntity.setEmail(registerDto.getEmail());
        emailEntity.setEmailAuthKey(UUID.randomUUID().toString());
        emailRepository.save(emailEntity);

        // 인증 메일 발송
        String email = registerDto.getEmail();
        String subject = "TableNow 이메일 인증";
        String text = mailComponents.getEmailAuthTemplate(savedEntity.getUser(), emailEntity.getEmailAuthKey());

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
        Optional<UsersEntity> optionalUser = userRepository.findByUser(user);
        if(optionalUser.isEmpty()) {
            return false;
        }
        UsersEntity users = optionalUser.get();

        Optional<EmailEntity> optionalEmail = emailRepository.findByEmailAuthKey(emailAuthKey);
        if (optionalEmail.isEmpty()) {
            return false;
        }
        EmailEntity emailUser = optionalEmail.get();
        if(emailUser.isEmailAuthYn()) {
            return false;
        }


        emailUser.setEmailAuthYn(true);
        emailUser.setEmailAuthDt(LocalDateTime.now());
        emailRepository.save(emailUser);

        users.setUserStatus(Status.ING);
        userRepository.save(users);

        return true;
    }

    //로그인
    @Override
    public LoginDto login(LoginDto loginDto) {
        // 1. 먼저 사용자 찾기
        UsersEntity user = userRepository.findByUser(loginDto.getUser())
                .orElseThrow(() -> new RuntimeException("해당 ID가 없습니다."));

        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 사용자 상태 확인
        if(user.getUserStatus() == Status.STOP) {
            throw new RuntimeException("해당 ID가 없습니다.");
        }

        // 4. 이메일 인증 확인 - userId로 조회
        EmailEntity emailAuth = emailRepository.findTopByUserIdOrderByIdDesc(user.getId())
                .orElseThrow(() -> new RuntimeException("해당 ID가 없습니다."));

        if(!emailAuth.isEmailAuthYn()) {
            throw new RuntimeException("가입 하신 이메일로 인증을 완료해주세요.");
        }

        // 5. 로그인 성공 처리
        LoginDto responseDto = userMapper.toLoginDto(user);
        String accessToken = tokenProvider.generateAccessToken(responseDto);
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
        // 1. 사용자 조회
        Optional<UsersEntity> optionalUsers = userRepository.findByUser(user);
        if (optionalUsers.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UsersEntity users = optionalUsers.get();

        // 2. 해당 사용자의 모든 상점 조회 후 삭제
        List<StoreEntity> userStores = storeRepository.findByUser(users);
        storeRepository.deleteAll(userStores);

        // 3. 사용자 상태 변경
        users.setUserStatus(Status.STOP);
        UsersEntity savedUser = userRepository.save(users);

        return new DeleteDto(savedUser.getUser(), savedUser.getRole());
    }

    // 내 정보 조회
    @Override
    public MyInfoDto myInfo(String user) {
        // 사용자 정보 조회
        UsersEntity userEntity = userRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // Entity를 DTO로 변환
        return MyInfoDto.builder()
                .user(userEntity.getUser())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .createAt(userEntity.getCreateAt())
                .build();
    }
}