package zerobase.tableNow.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.reservation.dto.ApprovalDto;
import zerobase.tableNow.domain.reservation.dto.ReservationDto;
import zerobase.tableNow.domain.reservation.service.ReservationService;

@Slf4j
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

    //예약 확정
    @PostMapping("approval")
    public ResponseEntity<ApprovalDto> approval(@RequestParam(name = "phone") String phone) {
        log.info("요청 폰 번호 + {}", phone);
        ApprovalDto response = reservationService.approve(phone);
        return ResponseEntity.ok(response);
    }
}
