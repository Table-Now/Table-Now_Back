package zerobase.tableNow.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.review.dto.ReviewDto;
import zerobase.tableNow.domain.review.dto.UpdateDto;
import zerobase.tableNow.domain.review.service.ReviewService;
import zerobase.tableNow.domain.store.entity.StoreEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "review")
public class ReviewController {
    private final ReviewService reviewService;

    //리뷰 등록
    @PostMapping("register")
    public ResponseEntity<ReviewDto> register(@RequestParam(name = "store") String store,
                                              @RequestBody ReviewDto reviewDto){
        return ResponseEntity.ok(reviewService.register(store,reviewDto));
    }

    //리뷰 목록
    @GetMapping("list")
    public ResponseEntity <List<ReviewDto>> list (){
        return ResponseEntity.ok().body(reviewService.list());
    }

    //리뷰 수정
    @PutMapping("update")
    public ResponseEntity<UpdateDto> update(@RequestParam(name = "id") Long id,
                                            @RequestBody ReviewDto reviewDto){
        return ResponseEntity.ok().body(reviewService.update(id,reviewDto));
    }

    //리뷰 삭제
    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@RequestParam(name = "id")Long id){
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
