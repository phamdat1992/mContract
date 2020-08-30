package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.ContractEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
	
	@Query(value = "select * " +
			"from contract c " +
			"where (:contractStatusId is null or c.fk_contract_status = :contractStatusId) " +
			"   and (:userId is null or c.fk_user = :userId) " +
			"   and (:bookmarkStar is null or c.bookmark_star = :bookmarkStar) " +
			"order by c.updated_at desc", nativeQuery = true)
	Page<ContractEntity> getContractByCondition(Long contractStatusId, Long userId, Pageable pageable, Boolean bookmarkStar); //s19, s15, s9, s5, s7, s3
	
	@Query(value = "select * " +
									"from contract c  " +
									"where DATEDIFF(c.expiry_date_signed, CURRENT_DATE()) / DATEDIFF(c.expiry_date_signed, c.created_at) < :percentTime " +
									"   and c.fk_user = :userId " +
									"order by c.updated_at desc",
			nativeQuery = true)
	Page<ContractEntity> getContractExpire30(Float percentTime, Long userId, Pageable pageable); // s13
	
	@Query(value = "select * " +
									"from contract c  " +
									"where c.id in ( " +
									"                 select distinct cu.fk_contract  " +
									"                 from contract_user cu  " +
									"                 where cu.fk_email = :emailId " +
									"               ) " +
									"order by c.updated_at desc",
							nativeQuery = true)
	Page<ContractEntity> getAllContract(Long emailId, Pageable pageable); // s1
	
	@Query(value = "select distinct c2.* " +
									"from contract_user cu, contract c2  " +
									"where c2.fk_user = :userId and cu.fk_contract = c2.id  " +
									"   and cu.fk_contract_status in (:contractStatusId) " +
									"order by c2.updated_at desc"
			, nativeQuery = true)
	Page<ContractEntity> getAllContractByContractStatus(Long userId, List<Long> contractStatusId, Pageable pageable); // s11, s17
	
	Optional<ContractEntity> findByIdAndFkUser(Long contractId, Long userId);
	
	
}