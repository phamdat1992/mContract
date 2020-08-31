package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.ContractUserEntity;
import vn.inspiron.mcontract.modules.Entity.EmailEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractUserRepository extends JpaRepository<ContractUserEntity, Long> {
	List<ContractUserEntity> getAllByFkContract(Long contractId);
	Optional<ContractUserEntity> getByFkContractAndFkEmail(Long contractId, Long mailId);
}