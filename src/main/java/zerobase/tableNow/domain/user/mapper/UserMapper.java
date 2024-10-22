package zerobase.tableNow.domain.user.mapper;

import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.user.dto.LoginDto;
import zerobase.tableNow.domain.user.dto.RegisterDto;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.UUID;

@Component
public class UserMapper {
    //회원가입 DTO -> Entity
    public UsersEntity toEntity(RegisterDto dto) {
        Role userRole = dto.isManagerYn() ? Role.MANAGER : Role.USER;

        return UsersEntity.builder()
                .userId(dto.getUserId())
                .name(dto.getName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .emailAuthYn(dto.isEmailAuthYn())
                .emailAuthDt(dto.getEmailAuthDt())
                .emailAuthKey(UUID.randomUUID().toString())
                .role(userRole)
                .managerYn(dto.isManagerYn())
                .userStatus(Status.REQ)
                .build();
    }

    //회원가입 Entity -> Dto
    public RegisterDto toDto(UsersEntity entity) {
        return RegisterDto.builder()
                .userId(entity.getUserId())
                .name(entity.getName())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .emailAuthYn(entity.isEmailAuthYn())
                .emailAuthDt(entity.getEmailAuthDt())
                .emailAuthKey(entity.getEmailAuthKey())
                .role(entity.getRole())
                .managerYn(entity.isManagerYn())
                .userStatus(entity.getUserStatus())
                .build();
    }

    //로그인  Entity -> Dto
    public LoginDto toLoginDto(UsersEntity entity){
        return LoginDto.builder()
                .userId(entity.getUserId())
                .password(entity.getPassword())
                .build();
    }
}
