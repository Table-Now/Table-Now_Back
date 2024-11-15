package zerobase.tableNow.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.user.entity.EmailEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
    Optional<EmailEntity> findByEmailAuthKey(String emailAuthKey);

    //Optional<EmailEntity> findByEmailEmail(String email);

    Optional<EmailEntity> findTopByUserIdOrderByIdDesc(Long userId);

    List<EmailEntity> findByUser(UsersEntity users);
}
