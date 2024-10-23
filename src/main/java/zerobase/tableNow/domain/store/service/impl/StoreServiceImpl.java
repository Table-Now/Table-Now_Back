package zerobase.tableNow.domain.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.store.dto.StoreDto;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.mapper.StoreMapper;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.store.service.StoreService;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;

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
    public List<StoreDto> getAllStores(String keyword) {
        List<StoreEntity> storeEntities;

        if (keyword == null || keyword.trim().isEmpty()) {
            storeEntities = storeRepository.findAll();
        } else {
            storeEntities = storeRepository.findByStoreNameContainingIgnoreCase(keyword.trim());
        }

        return storeEntities.stream()
                .map(storeMapper::convertToDto)
                .collect(Collectors.toList());
    }

}
