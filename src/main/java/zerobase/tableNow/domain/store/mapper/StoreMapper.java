package zerobase.tableNow.domain.store.mapper;

import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.store.dto.StoreDto;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.dto.LoginDto;
import zerobase.tableNow.domain.user.dto.RegisterDto;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.UUID;

@Component
public class StoreMapper {
    public StoreEntity toStoreEntity(StoreDto storeDto, UsersEntity optionalUsers) {
        //상점등록 DTO -> Entity
        return StoreEntity.builder()
                .userId(optionalUsers)
                .storeName(storeDto.getStoreName())
                .storeLocation(storeDto.getStoreLocation())
                .storeImg(storeDto.getStoreImg())
                .storeContents(storeDto.getStoreContents())
                .storeOpen(storeDto.getStoreOpen())
                .storeClose(storeDto.getStoreClose())
                .storeWeekOff(storeDto.getStoreWeekOff())
                .build();

    }

    //상점등록 Entity -> Dto
    public StoreDto toStoreDto(StoreEntity storeEntity) {
        return StoreDto.builder()
                .userId(storeEntity.getUserId().getUserId())
                .storeName(storeEntity.getStoreName())
                .storeLocation(storeEntity.getStoreLocation())
                .storeImg(storeEntity.getStoreImg())
                .storeContents(storeEntity.getStoreContents())
                .storeOpen(storeEntity.getStoreOpen())
                .storeClose(storeEntity.getStoreClose())
                .storeWeekOff(storeEntity.getStoreWeekOff())
                .build();
    }

}
