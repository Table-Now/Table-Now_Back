package zerobase.tableNow.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "store")
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity userId;
    private String storeName;
    private String storeLocation;
    private String storeImg;
    private String storeContents;
    private String storeOpen;
    private String storeClose;
    private String storeWeekOff;

}
