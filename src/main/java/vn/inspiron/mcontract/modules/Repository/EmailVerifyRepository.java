package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.ContractEntity;
import vn.inspiron.mcontract.modules.Entity.EmailVerifyTokenEntity;

import java.util.Optional;

@Repository
public interface EmailVerifyRepository extends JpaRepository<EmailVerifyTokenEntity, Long> {

    public Optional<EmailVerifyTokenEntity> findByToken(String token);

    @Query(value = "select * " +
            "from email_verify_token" +
            "where fk_user = :userId " +
            "   and (is_active = true) ",
            nativeQuery = true
    )
    public Optional<EmailVerifyTokenEntity> getEmailVerifyToken(Long userId);
}
