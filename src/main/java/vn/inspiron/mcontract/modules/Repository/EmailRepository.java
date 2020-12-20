package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.EmailEntity;
import vn.inspiron.mcontract.modules.Entity.MstEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
    Optional<EmailEntity> findByEmail(String email);
    List<EmailEntity> findByEmailIn(List<String> emails);
    Optional<EmailEntity> findByFkUser(Long userId);
}