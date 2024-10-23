package zerobase.tableNow.domain.store.service;

import zerobase.tableNow.domain.constant.SortType;
import zerobase.tableNow.domain.store.dto.StoreDto;

import java.util.List;

public interface  StoreService {
    //상점 등록
    StoreDto register(StoreDto storeDto);

    //모든 상점 목록
    List<StoreDto> getAllStores(String keyword, SortType sortType);



}
