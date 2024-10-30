package zerobase.tableNow.domain.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.constant.Status;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterDto {
    private String user;
    private String name;
    private String password;
    private String email;
    private String phone;
    private boolean emailAuthYn; //메일 인증 했는지
    private LocalDateTime emailAuthDt; //이메일 인증 날짜
    private  String emailAuthKey; // 회원가입할때 생성해서 메일인증할때 쓰는 Key

    @Enumerated(EnumType.STRING)
    private Role role; //사용자타입

    private boolean managerYn; //매니저 판단
    private Status userStatus; //이용가능한 상태, 정지상태
}
