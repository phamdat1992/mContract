package vn.amitgroup.digitalsignatureapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.amitgroup.digitalsignatureapi.entity.SignerContract;
import vn.amitgroup.digitalsignatureapi.entity.User;

public interface SignerContractRepository extends JpaRepository<SignerContract,Integer> {
    @Query(value = "select u from Signer s join s.signerContracts sc join s.user u" 
    +" where sc.contractRole = 'CREATER' and sc.contract.id =:contractId")
    User  getUserCreateContract(@Param("contractId") String contractId);
    @Query(value = "select sc from SignerContract sc join sc.signer s join sc.contract c where s.email =:email and c.id in :contractList")
    List<SignerContract> findBySignerContractList(@Param("contractList") List<String> contractList, @Param("email") String email);
    List<SignerContract> findByContract_Id(@Param("contractId") String contractId);
    List<SignerContract> findByContract_IdOrderByCreatedTimeAsc(@Param("contractId") String contractId);
    SignerContract findByContract_IdAndSigner_Email(@Param("contractId") String contractId,@Param("email") String email);
}
