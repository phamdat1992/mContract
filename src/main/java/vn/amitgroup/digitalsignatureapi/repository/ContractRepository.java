package vn.amitgroup.digitalsignatureapi.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.amitgroup.digitalsignatureapi.entity.Contract;

public interface ContractRepository extends  JpaRepository<Contract, String> {
	@Query(value =" SELECT count(*),'TOTAL' status FROM users u, signer s, signer_contract sc, contract c "
	+" WHERE u.user_id=s.user_id AND s.signer_id = sc.signer_id AND sc.contract_id = c.contract_id and u.email=:email AND sc.is_delected is not true GROUP BY status UNION"
	+" SELECT count(*),'PROCESSING' status FROM users u, signer s, signer_contract sc, contract c "
	+" WHERE u.user_id=s.user_id AND sc.is_signed is not true AND sc.contract_status not in ('AUTHENTICATIONFAIL','COMPLETE','EXPIRED','CANCEL') AND s.signer_id = sc.signer_id AND sc.contract_id = c.contract_id and u.email=:email AND sc.is_delected is not true group by status  UNION"
	+" SELECT count(*),'WAITINGFORPARTNER' status FROM (SELECT c.contract_id FROM contract c,signer_contract sc,signer s,users u WHERE c.contract_id=sc.contract_id AND u.user_id=s.user_id AND s.signer_id=sc.signer_id AND sc.is_delected is not true  AND u.email=:email AND sc.contract_status= 'WAITINGFORPARTNER'"
	+" GROUP BY c.contract_id HAVING (SELECT COUNT(*) FROM contract c1,signer_contract sc1,signer s1 WHERE c1.contract_id=sc1.contract_id AND s1.signer_id=sc1.signer_id AND s1.email !=:email and c1.contract_id=c.contract_id and sc1.is_signed is not true) >0) as temp UNION"
	+" SELECT count(*),'CANCEL' status FROM users u, signer s, signer_contract sc, contract c "
	+" WHERE u.user_id=s.user_id AND sc.contract_status= 'CANCEL' AND s.signer_id = sc.signer_id AND sc.contract_id = c.contract_id and u.email=:email AND sc.is_delected is not true group by status UNION"
	+" SELECT count(*),'EXPIRED' status FROM users u, signer s, signer_contract sc, contract c "
	+" WHERE u.user_id=s.user_id AND sc.contract_status= 'EXPIRED' AND s.signer_id = sc.signer_id AND sc.contract_id = c.contract_id and u.email=:email AND sc.is_delected is not true group by status UNION"
	+" SELECT count(*),'COMPLETE' status FROM users u, signer s, signer_contract sc, contract c "
	+" WHERE u.user_id=s.user_id AND sc.contract_status= 'COMPLETE' AND s.signer_id = sc.signer_id AND sc.contract_id = c.contract_id and u.email=:email AND sc.is_delected is not true group by status UNION"
	+" SELECT count(*),'AUTHENTICATIONFAIL' status FROM users u, signer s, signer_contract sc, contract c "
	+" WHERE u.user_id=s.user_id AND sc.contract_status= 'AUTHENTICATIONFAIL' AND s.signer_id = sc.signer_id AND sc.contract_id = c.contract_id and u.email=:email AND sc.is_delected is not true group by status",nativeQuery = true)
	List<Object[]> getStatisticalData(@Param("email") String email);
	@Query(value = "SELECT c.* FROM signer_contract sc , contract c"
			+" where sc.contract_id=:contractId and sc.email_signer= :email and sc.contract_id=c.contract_id",nativeQuery = true)
	Contract getByIdAndUserEmail(@Param("contractId") String contractId,@Param("email") String email);
//	@Query(value = "SELECT c.* FROM signer_contract sc , contract c, signer s, users u"
//    +" where c.contract_id in (:contractIds) and sc.signer_id=s.signer_id and s.user_id=u.user_id and u.email = :email and sc.contract_id=c.contract_id",nativeQuery = true)
@Query(value = "SELECT c.* FROM signer_contract sc , contract c"
		+" where c.contract_id in (:contractIds) and sc.email_signer = :email and sc.contract_id=c.contract_id",nativeQuery = true)
	List<Contract> getByListIdAndUserEmail(@Param("contractIds") List<String> contractIds,@Param("email") String email);
	@Query(value = "select c from SignerContract sc join sc.signer s join sc.contract c where s.email =:email and c.id=:id and (c.isValidTaxcode is true or sc.contractRole = 'CREATER')")
	Contract findByIdAndSignersEmail(String id,String email);
	@Query(value = "SELECT c.* FROM signer_contract sc , contract c, signer s, users u" 
    +" where sc.contract_id=:contractId and sc.signer_id=s.signer_id and s.user_id=u.user_id and sc.contract_id=c.contract_id and u.email = :email and sc.contract_role='CREATER'",nativeQuery = true)
	Contract getByIdAndUserCreate(@Param("contractId") String contractId,@Param("email") String email);
	@Query(value = "select DISTINCT c.id from Contract c inner join SignerContract sc on c.id =sc.contract.id inner join Signer s on s.id = sc.signer.id inner join User u on s.user.id = :id")
	List<String> getAllContractByUser(Integer id);
}
