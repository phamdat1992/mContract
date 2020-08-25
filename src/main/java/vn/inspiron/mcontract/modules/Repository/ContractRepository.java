package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.ContractEntity;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
	
	Page<ContractEntity> getContractEntitiesByFkContractStatus(Long contractStatusId, Pageable pageable); //s19, s15, s9, s5
	
	@Query(value = "select * " +
									"from contract c  " +
									"where DATEDIFF(c.expiry_date_signed, CURRENT_DATE()) / DATEDIFF(c.expiry_date_signed, c.created_at) < :percentTime",
			nativeQuery = true)
	Page<ContractEntity> getContractExpire30(Float percentTime, Pageable pageable); // s13
	
	Page<ContractEntity> getContractEntitiesByBookmarkStar(boolean bookmarkStar, Pageable pageable); // s7
	
	Page<ContractEntity> getContractEntitiesByFkUser(Long userId, Pageable pageable); // s3
	
	@Query(value = "select * " +
									"from contract c  " +
									"where c.id in ( " +
									"                 select distinct cu.fk_contract  " +
									"                 from contract_user cu  " +
									"                 where cu.fk_email = :emailId " +
									"               )",
							nativeQuery = true)
	Page<ContractEntity> getAllContract(Long emailId, Pageable pageable); // s1
	
	@Query(value = "select distinct c2.* " +
									"from contract_user cu, contract c2  " +
									"where c2.fk_user = 1 and cu.fk_contract = c2.id  " +
									"   and cu.fk_contract_status in (7, 8, 9, 10, 11, 12)"
			, nativeQuery = true)
	Page<ContractEntity> getAllContractByContractStatus(Long userId, List<Long> contractStatusId, Pageable pageable); // s11, s17
}