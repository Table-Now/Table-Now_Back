package zerobase.tableNow.domain.review.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.review.dto.ReviewDto;
import zerobase.tableNow.domain.review.dto.UpdateDto;
import zerobase.tableNow.domain.review.entity.ReviewEntity;
import zerobase.tableNow.domain.review.mapper.ReviewMapper;
import zerobase.tableNow.domain.review.repository.ReviewRepository;
import zerobase.tableNow.domain.review.service.ReviewService;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    /**
     * 리뷰등록
     * @param store
     * @param reviewDto
     * @return 리뷰내용
     */
    @Override
    public ReviewDto register(String store, ReviewDto reviewDto) {
        // 사용자 확인
        UsersEntity user = userRepository.findByUserId(reviewDto.getUserId())
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        // ReviewEntity 생성 및 저장
        ReviewEntity reviewEntity = reviewMapper.toReviewEntity(reviewDto, user);
        ReviewEntity savedEntity = reviewRepository.save(reviewEntity);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return reviewMapper.toReviewDto(savedEntity);
    }

    /**
     * 리뷰목록
     * @return 리뷰목록
     */
    @Override
    public List<ReviewDto> list() {
        List<ReviewEntity> reviewEntities = reviewRepository
                .findAllByOrderByCreateAtDesc();

        return reviewEntities.stream()
                .map(reviewMapper::toReviewDto)
                .collect(Collectors.toList());
    }

    /**
     * 리뷰 수정
     * @param id
     * @param reviewDto
     * @return 리뷰 수정
     */
    @Override
    public UpdateDto update(Long id, ReviewDto reviewDto) {
        ReviewEntity existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 리뷰가 없습니다."));

        // 기존 리뷰 엔티티 업데이트
        existingReview.setStore(reviewDto.getStore());
        existingReview.setContents(reviewDto.getContents());

        // 변경된 엔티티 저장
        ReviewEntity updatedReview = reviewRepository.save(existingReview);

        // 업데이트된 엔티티를 DTO로 변환하여 반환
        return reviewMapper.toUpdateDto(updatedReview);
    }

    /**
     * 리뷰 삭제
     * @param id
     */
    @Override
    public void delete(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Review with id " + id + " not found");
        }
    }
}
