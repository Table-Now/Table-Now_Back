package zerobase.tableNow.domain.store.service;

import zerobase.tableNow.domain.store.dto.StoreDto;

public interface  StoreService {
    //상점 등록
    StoreDto register(StoreDto storeDto);
}
