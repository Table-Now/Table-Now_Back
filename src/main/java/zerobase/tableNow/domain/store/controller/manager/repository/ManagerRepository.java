package zerobase.tableNow.domain.store.controller.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.List;

@Repository
public interface ManagerRepository extends JpaRepository<StoreEntity,Long> {

    //해당 매니저 상점 목록
    List<StoreEntity> findByUserId(UsersEntity userId);
}
