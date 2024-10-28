package zerobase.tableNow.domain.reservation.dto;

import lombok.*;
import zerobase.tableNow.domain.constant.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private String userId;
    private String phone;

    private String storeName; //상점이름
    private LocalDateTime reserDateTime;//예약 날짜, 시간
    private Integer peopleNb; // 예약인원


    private Boolean reserCheck; //예약 10분전 체크
    private Status reservationStatus; //이용가능한 상태, 정지상태

}
