package zerobase.tableNow.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.store.dto.StoreDto;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.store.service.StoreService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/")
public class StoreController {
    private final StoreService storeService;
    @PostMapping ("register")
    public ResponseEntity<StoreDto>register(@RequestBody StoreDto storeDto){
        return ResponseEntity.ok().body(storeService.register(storeDto));

    }
}
