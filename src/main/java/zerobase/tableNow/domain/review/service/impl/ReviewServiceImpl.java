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
     * 리뷰 등록
     * @param store 상점 이름
     * @param reviewDto 리뷰 DTO
     * @return 등록된 리뷰 내용
     */
    @Override
    public ReviewDto register(String store, ReviewDto reviewDto) {
        // 사용자 확인
        UsersEntity user = userRepository.findByUser(reviewDto.getUser())
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        // ReviewEntity 생성 및 저장
        ReviewEntity reviewEntity = reviewMapper.toReviewEntity(reviewDto, user);
        ReviewEntity savedEntity = reviewRepository.save(reviewEntity);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return reviewMapper.toReviewDto(savedEntity);
    }


    /**
     * 리뷰 목록 조회
     * @return 리뷰 목록
     */
    @Override
    public List<ReviewDto> list() {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByOrderByCreateAtDesc();
        return reviewEntities.stream()
                .map(reviewMapper::toReviewDto)
                .collect(Collectors.toList());
    }

    /**
     * 리뷰 수정
     * @param user 사용자 ID
     * @param reviewDto 수정할 리뷰 DTO
     * @return 수정된 리뷰 내용
     */
    @Override
    public UpdateDto update(String user, ReviewDto reviewDto) {
        // 사용자 확인
        UsersEntity users = userRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        ReviewEntity existingReview = reviewRepository.findByUser(users)
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
     * @param user 사용자 ID
     */
    @Override
    public void delete(String user) {
        UsersEntity users = userRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Review with user id " + user + " not found"));

        if (reviewRepository.existsByUser(users)) {
            reviewRepository.deleteByUser(users);
        } else {
            throw new EntityNotFoundException("Review with user id " + users + " not found");
        }
    }
}
