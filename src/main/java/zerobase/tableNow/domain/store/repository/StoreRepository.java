package zerobase.tableNow.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity,Long> {

    Optional<StoreEntity> findByStoreName(String storeName);

    List<StoreEntity> findByStoreNameContainingIgnoreCase(String trim);

//    List<StoreEntity> findByStoreNameContainingIgnoreCaseOrderByRatingDesc(String keyword);
//    List<StoreEntity> findByStoreNameContainingIgnoreCaseOrderByRatingAsc(String keyword);
//    List<StoreEntity> findByStoreNameContainingIgnoreCaseOrderByStoreNameAsc(String keyword);
//    List<StoreEntity> findByStoreNameContainingIgnoreCaseOrderByStoreNameDesc(String keyword);
//
//    List<StoreEntity> findAllByOrderByRatingDesc();
//    List<StoreEntity> findAllByOrderByRatingAsc();
//    List<StoreEntity> findAllByOrderByStoreNameAsc();
//    List<StoreEntity> findAllByOrderByStoreNameDesc();
}
