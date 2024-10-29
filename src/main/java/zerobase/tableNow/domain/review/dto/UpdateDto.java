package zerobase.tableNow.domain.review.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {
    private String userId;
    private String store; // 상점
    private String contents; // 리뷰내용
}
