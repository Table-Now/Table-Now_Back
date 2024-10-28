package zerobase.tableNow.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.reservation.dto.ReservationDto;
import zerobase.tableNow.domain.reservation.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation/")
public class ReservationController {
    private final ReservationService reservationService;
    //예약요청
    @PostMapping("request")
    public ResponseEntity<ReservationDto> request(@RequestBody ReservationDto reservationDto){
        return ResponseEntity.ok().body(reservationService.request(reservationDto));
    }
}
