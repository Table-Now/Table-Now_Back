package zerobase.tableNow.domain.store.controller.manager.service;

import zerobase.tableNow.domain.store.controller.manager.dto.ConfirmDto;
import zerobase.tableNow.domain.store.controller.manager.dto.ManagerDto;

import java.util.List;

public interface ManagerService {
    // 해당 매니저 상점 목록
    List<ManagerDto> managerList(ManagerDto managerDto);

    //예약 정보 확인
    List<ConfirmDto> confirmList(String store);



}
