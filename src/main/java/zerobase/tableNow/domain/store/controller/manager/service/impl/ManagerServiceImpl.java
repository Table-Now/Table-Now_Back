package zerobase.tableNow.domain.store.controller.manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
import zerobase.tableNow.domain.store.controller.manager.dto.ConfirmDto;
import zerobase.tableNow.domain.store.controller.manager.dto.ManagerDto;
import zerobase.tableNow.domain.store.controller.manager.repository.ManagerRepository;
import zerobase.tableNow.domain.store.controller.manager.service.ManagerService;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    //매니져전용 상점 목록
    @Override
    public List<ManagerDto> managerList(ManagerDto managerDto) {
        UsersEntity user = userRepository.findByUserId(managerDto.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        List<StoreEntity> storeEntities =
                storeRepository.findByUserId(user);

        // 조회된 상점 목록을 ManagerDto 리스트로 변환
        return storeEntities.stream().map(store -> ManagerDto.builder()
                .userId(user.getUserId())
                .storeName(store.getStoreName())
                .storeLocation(store.getStoreLocation())
                .storeImg(store.getStoreImg())
                .storeContents(store.getStoreContents())
                .rating(store.getRating())
                .storeOpen(store.getStoreOpen())
                .storeClose(store.getStoreClose())
                .storeWeekOff(store.getStoreWeekOff())
                .build()
        ).collect(Collectors.toList());
    }

    //매니저전용 예약정보 확인
    @Override
    public List<ConfirmDto> confirmList(String storeName) {
        List<ReservationEntity> reservations = managerRepository.findByStoreName_StoreNameAndReservationStatus(storeName, Status.ING);

        return reservations.stream()
                .map(reservation -> ConfirmDto.builder()
                        .storeName(reservation.getStoreName().getStoreName())
                        .phone(reservation.getPhone())
                        .reserDateTime(reservation.getReserDateTime())
                        .peopleNb(reservation.getPeopleNb())
                        .reservationStatus(reservation.getReservationStatus())
                        .build())
                .collect(Collectors.toList());
    }

}
