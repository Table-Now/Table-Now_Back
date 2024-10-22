package zerobase.tableNow.domain.user.service;

import zerobase.tableNow.domain.user.dto.LoginDto;
import zerobase.tableNow.domain.user.dto.RegisterDto;

public interface UserService {
    //회원가입
    RegisterDto register(RegisterDto registerDto);

    //이메일 인증
    boolean emailAuth(String userId, String authKey);

    //로그인
    void login(LoginDto loginDto);


    
}
