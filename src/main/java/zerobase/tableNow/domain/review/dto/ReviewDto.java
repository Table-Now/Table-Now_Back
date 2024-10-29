package zerobase.tableNow.domain.review.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto{
    private String userId;
    private String store; // 상점
    private String contents; // 리뷰내용
    private Role role; //사용자타입

}
