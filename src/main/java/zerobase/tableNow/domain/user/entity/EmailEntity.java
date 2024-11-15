package zerobase.tableNow.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "EmailAuth")
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")  // users 테이블의 id와 매핑
    private UsersEntity user;

    private String email;

    private boolean emailAuthYn = false; //메일 인증 했는지
    private LocalDateTime emailAuthDt; //이메일 인증 날짜
    private  String emailAuthKey; // 회원가입할때 생성해서 메일인증할때 쓰는 Key
}
