package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.EmailVerifyTokenEntity;

import java.util.Optional;

@Repository
public interface EmailVerifyRepository extends JpaRepository<EmailVerifyTokenEntity, Long> {

    public Optional<EmailVerifyTokenEntity> findByToken(String token);

}
