package zerobase.tableNow.domain.reservation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.reservation.dto.ReservationDto;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.reservation.mapper.ReservationMapper;
import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
import zerobase.tableNow.domain.reservation.service.ReservationService;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ReservationMapper reservationMapper;

    //예약 요청
    @Override
    public ReservationDto request(ReservationDto reservationDto) {
        UsersEntity users = userRepository
                .findByUserId(reservationDto.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 ID가 없습니다."));

        StoreEntity store = storeRepository
                .findByStoreName(reservationDto.getStoreName())
                .orElseThrow(() -> new RuntimeException("해당 가게가 없습니다."));

        ReservationEntity reservationEntity = reservationMapper.toReserEntity(reservationDto, users, store);
        ReservationEntity saveEntity = reservationRepository.save(reservationEntity);

        return reservationMapper.toReserDto(saveEntity);
    }
}
