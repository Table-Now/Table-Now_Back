package zerobase.tableNow.domain.store.controller.manager.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.tableNow.domain.store.controller.manager.dto.ManagerDto;
import zerobase.tableNow.domain.store.controller.manager.service.ManagerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/")
public class ManagerController {
    private final ManagerService managerService;

    // 해당 매니저의 상점 목록 조회
    @GetMapping("list")
    public ResponseEntity<List<ManagerDto>> managerList(@RequestBody ManagerDto managerDto) {
        List<ManagerDto> managerDtoList = managerService.managerList(managerDto);
        return ResponseEntity.ok(managerDtoList);
    }

    // 예약 정보 확인
    @GetMapping("confirm")
    public ResponseEntity<List<ManagerDto>> confirmList(@RequestBody ManagerDto managerDto){
        List<ManagerDto> managerDtoList = managerService.confirmList(managerDto);
        return ResponseEntity.ok().body(managerDtoList);
    }
}
