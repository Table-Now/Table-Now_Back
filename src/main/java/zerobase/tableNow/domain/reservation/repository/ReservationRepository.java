package zerobase.tableNow.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.store.entity.StoreEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    //중복 예약 체크
    boolean existsByStoreNameAndReserDateTimeBetween(StoreEntity store
            , LocalDateTime startTime
            , LocalDateTime endTime);

    //폰번호으로 예약 승인
    Optional<ReservationEntity> findByPhone(String phone);

}