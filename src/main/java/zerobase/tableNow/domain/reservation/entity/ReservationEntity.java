package zerobase.tableNow.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reservation")
public class ReservationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity userId;
    private String phone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeName")
    private StoreEntity storeName; //상점이름
    private String reserDate; // 예약날짜
    private Integer peopleNb; // 예약인원

    private Boolean reserCheck; //예약 10분전 체크
    private Status reservationStatus; //이용가능한 상태, 정지상태

}
