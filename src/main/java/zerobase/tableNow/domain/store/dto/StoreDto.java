package zerobase.tableNow.domain.store.dto;

import lombok.*;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoreDto {
    private String userId;
    private String storeName;
    private String storeLocation;
    private String storeImg;
    private String storeContents;
    private String storeOpen;
    private String storeClose;
    private String storeWeekOff;
}
