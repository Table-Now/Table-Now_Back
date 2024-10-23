package zerobase.tableNow.domain.store.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.store.dto.StoreDto;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.store.service.StoreService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/")
public class StoreController {
    private final StoreService storeService;

    //상점 등록
    @PostMapping ("register")
    public ResponseEntity<StoreDto> register(@RequestBody StoreDto storeDto){
        return ResponseEntity.ok().body(storeService.register(storeDto));

    }

    // 상점 목록
    @GetMapping("list")
    public ResponseEntity<List<StoreDto>> list() {
        return ResponseEntity.ok().body(storeService.getAllStores());
    }
}
