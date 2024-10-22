package zerobase.tableNow.domain.user.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginDto {
    private String userId;
    private String password;
}
