package zerobase.tableNow.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.user.dto.LoginDto;
import zerobase.tableNow.domain.user.dto.RePasswordDto;
import zerobase.tableNow.domain.user.dto.RegisterDto;
import zerobase.tableNow.domain.user.service.UserService;

import javax.naming.AuthenticationException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/")
public class UserController {
    private  final UserService userService;
    //회원가입
    @PostMapping("register")
    public ResponseEntity<RegisterDto> register(
            @RequestBody RegisterDto registerDto){

        return ResponseEntity.ok().body(userService.register(registerDto));
    }

    @GetMapping("email-auth")
    public ResponseEntity<?> emailAuth(@RequestParam("id") String userId,
                                       @RequestParam("key") String authKey) {
        boolean result = userService.emailAuth(userId, authKey);

        if (result) {
            return ResponseEntity.ok().body(Map.of(
                    "message", "이메일 인증이 완료되었습니다.",
                    "success", true
            ));
        }

        return ResponseEntity.badRequest().body(Map.of(
                "message", "이메일 인증에 실패했습니다.",
                "success", false
        ));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            // 로그인 성공 시 JWT 토큰 포함된 LoginDto 반환
            LoginDto responseDto = userService.login(loginDto);
            return ResponseEntity.ok(responseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 실패: " + e.getMessage());
        }
    }
    //비밀번호 재설정
    @PostMapping("/repassword")
    public ResponseEntity<String> rePassword(@RequestBody RePasswordDto rePasswordDto) {
        return ResponseEntity.ok().body(userService.rePassword(rePasswordDto));
    }

}

