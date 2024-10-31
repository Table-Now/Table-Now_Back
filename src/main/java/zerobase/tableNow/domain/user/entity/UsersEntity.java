package zerobase.tableNow.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.constant.Status;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Users")
public class UsersEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String user;
    private String name;
    private String password;
    private String email;
    private String phone;

    private boolean emailAuthYn; //메일 인증 했는지
    private LocalDateTime emailAuthDt; //이메일 인증 날짜
    private  String emailAuthKey; // 회원가입할때 생성해서 메일인증할때 쓰는 Key
    private boolean managerYn; //매니저 판단

    private Role role; //사용자타입
    private Status userStatus; //이용가능한 상태, 정지상태

}
