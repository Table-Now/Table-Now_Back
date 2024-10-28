package zerobase.tableNow.domain.store.controller.manager.dto;

import jakarta.persistence.Column;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManagerDto {
    private String userId;
    private String phone;

    private String storeName;
    private String storeLocation;
    private String storeImg;
    private String storeContents;

    private Integer rating = 0; // 별점
    private String storeOpen;
    private String storeClose;
    private String storeWeekOff;
}
