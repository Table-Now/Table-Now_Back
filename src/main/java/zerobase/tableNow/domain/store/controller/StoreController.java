package zerobase.tableNow.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.constant.SortType;
import zerobase.tableNow.domain.store.dto.StoreDto;
import zerobase.tableNow.domain.store.service.StoreService;

import java.util.List;

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
    public ResponseEntity<List<StoreDto>> list(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sortType", required = false) String sortType
    ) {
        return ResponseEntity.ok().body(
                storeService.getAllStores(keyword,
                        sortType != null ? SortType.valueOf(sortType) : null)
        );
    }

    //상점 수정
    @PutMapping("update")
    public ResponseEntity<StoreDto> update(@RequestParam(name = "id") Long id ,
                                           @RequestBody StoreDto storeDto){
        return ResponseEntity.ok().body(storeService.update(id,storeDto));

    }
    //상점 상세정보
    @GetMapping("detail")
    public ResponseEntity<StoreDto> detail(@RequestParam(name = "id") Long id){
        return ResponseEntity.ok().body(storeService.detail(id));

    }
    //상점 삭제
    @DeleteMapping("delete")
    public ResponseEntity<Void> delete(@RequestParam(name = "id") Long id) {
        storeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
