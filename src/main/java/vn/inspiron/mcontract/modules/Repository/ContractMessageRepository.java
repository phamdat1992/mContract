package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.ContractMessageEntity;
import vn.inspiron.mcontract.modules.Entity.ContractUserEntity;

import java.util.List;

@Repository
public interface ContractMessageRepository extends JpaRepository<ContractMessageEntity, Long> {
}