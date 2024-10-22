package zerobase.tableNow.domain.user.service;

import zerobase.tableNow.domain.user.dto.RegisterDto;

public interface UserService {
    //회원가입
    RegisterDto register(RegisterDto registerDto);

    boolean emailAuth(String userId, String authKey);
}
