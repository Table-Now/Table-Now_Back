package zerobase.tableNow.domain.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.constant.SortType;
import zerobase.tableNow.domain.store.dto.StoreDto;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.mapper.StoreMapper;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.store.service.StoreService;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    //상점등록
    @Override
    public StoreDto register(StoreDto storeDto) {
        UsersEntity optionalUsers = userRepository.findByUserId(storeDto.getUserId())
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));
        Optional<StoreEntity> optionalStoreEntity = storeRepository.findByStoreName(storeDto.getStoreName());

        if (optionalStoreEntity.isPresent()){
            log.info("해당 상점이 존재합니다.");
            throw new RuntimeException("해당 상점이 존재합니다.");
        }

        // DTO -> Entity 변환 및 저장
        StoreEntity storeEntity = storeMapper.toStoreEntity(storeDto, optionalUsers);
        StoreEntity saveEntity = storeRepository.save(storeEntity);

        return storeMapper.toStoreDto(saveEntity);
    }

    //상점 목록
    @Override
    public List<StoreDto> getAllStores(String keyword, SortType sortType) {
        List<StoreEntity> storeEntities;

        // 1. 키워드 검색
        if (keyword != null && !keyword.trim().isEmpty()) {
            storeEntities = storeRepository.findByStoreNameContainingIgnoreCase(keyword.trim());
        } else {
            storeEntities = storeRepository.findAll();
        }

        // 2. 정렬 적용 (정렬 타입이 있는 경우에만)
        if (sortType != null) {
            switch (sortType) {
                case RATING_HIGH:
                    storeEntities.sort((a, b) -> compareRatings(b.getRating(), a.getRating()));
                    break;
                case RATING_LOW:
                    storeEntities.sort((a, b) -> compareRatings(a.getRating(), b.getRating()));
                    break;
                case NAME_ASC:
                    storeEntities.sort((a, b) -> compareNames(a.getStoreName(), b.getStoreName()));
                    break;
                case NAME_DESC:
                    storeEntities.sort((a, b) -> compareNames(b.getStoreName(), a.getStoreName()));
                    break;
            }
        }

        return storeEntities.stream()
                .map(storeMapper::convertToDto)
                .collect(Collectors.toList());
    }

    //상점 수정
    @Override
    public StoreDto update(Long id, StoreDto storeDto) {
        StoreEntity storeUpdate = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상점이 없습니다"));


        UsersEntity currentUser = storeUpdate.getUserId();

        storeUpdate.setUserId(currentUser); // 기존 사용자 정보 유지
        storeUpdate.setStoreName(storeDto.getStoreName());
        storeUpdate.setStoreLocation(storeDto.getStoreLocation());
        storeUpdate.setStoreImg(storeDto.getStoreImg());
        storeUpdate.setStoreContents(storeDto.getStoreContents());
        storeUpdate.setRating(storeDto.getRating());
        storeUpdate.setStoreOpen(storeDto.getStoreOpen());
        storeUpdate.setStoreClose(storeDto.getStoreClose());
        storeUpdate.setStoreWeekOff(storeDto.getStoreWeekOff());
        storeUpdate.setUpdateAt(LocalDateTime.now());


        StoreEntity updatedStore = storeRepository.save(storeUpdate);


        return storeMapper.convertToDto(updatedStore);
    }

    //상점 상세정보
    @Override
    public StoreDto detail(Long id) {
        StoreEntity storeDetail = storeRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("해당 상점이 없습니다."));

        return storeMapper.convertToDto(storeDetail);
    }

    // Null 처리를 위한 헬퍼 메소드들
    private int compareRatings(Integer rating1, Integer rating2) {
        // null을 0으로 처리
        int r1 = rating1 == null ? 0 : rating1;
        int r2 = rating2 == null ? 0 : rating2;
        return Integer.compare(r1, r2);
    }

    private int compareNames(String name1, String name2) {
        // null을 빈 문자열로 처리
        String n1 = name1 == null ? "" : name1;
        String n2 = name2 == null ? "" : name2;
        return n1.compareTo(n2);
    }


}
