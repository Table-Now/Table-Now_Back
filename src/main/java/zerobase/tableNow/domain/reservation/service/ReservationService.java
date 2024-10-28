package zerobase.tableNow.domain.reservation.service;

import zerobase.tableNow.domain.reservation.dto.ApprovalDto;
import zerobase.tableNow.domain.reservation.dto.ReservationDto;

public interface ReservationService {
    //예약요청
    ReservationDto request(ReservationDto reservationDto);

    //예약 확정
    ApprovalDto approve(String phone);
}
