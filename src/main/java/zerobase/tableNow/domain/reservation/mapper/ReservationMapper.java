package zerobase.tableNow.domain.reservation.mapper;

import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.reservation.dto.ReservationDto;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Component
public class ReservationMapper {
    public ReservationEntity toReserEntity(ReservationDto reservationDto,
                                           UsersEntity optionalUsers,
                                           StoreEntity optionalStore){
        //예약 요청 DTO -> Entity
        return ReservationEntity.builder()
                .userId(optionalUsers)
                .phone(optionalUsers.getPhone())
                .storeName(optionalStore)
                .reserDateTime(reservationDto.getReserDateTime())
                .peopleNb(reservationDto.getPeopleNb())
                .reserCheck(false)
                .reservationStatus(Status.REQ)
                .build();

    }
    public ReservationDto toReserDto(ReservationEntity reservationEntity){
        return ReservationDto.builder()
                .userId(reservationEntity.getUserId().getUserId())
                .phone(reservationEntity.getPhone())
                .storeName(reservationEntity.getStoreName().getStoreName())
                .reserDateTime(reservationEntity.getReserDateTime())
                .peopleNb(reservationEntity.getPeopleNb())
                .reserCheck(reservationEntity.getReserCheck())
                .reservationStatus(reservationEntity.getReservationStatus())
                .build();
    }
}
