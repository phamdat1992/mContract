package vn.amitgroup.digitalsignatureapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.amitgroup.digitalsignatureapi.entity.Signer;

public interface SignerRepository extends JpaRepository<Signer, Integer> {
    @Query(value = "select s from SignerContract sc join sc.signer s join sc.contract c where c.id =:id")
     List<Signer> findByContractId(@Param("id") String id);
    @Query(value = "select sc.emailSigner from SignerContract sc where  sc.contract.id=:id and sc.isSigned = true")
    List<String> findAllByIdAndStatus(@Param("id") String id);
    Signer findByEmailAndTaxCodeAndFullName(String email,String taxCode,String fullName);
    @Query(value = "select s from SignerContract sc join sc.signer s join sc.contract c where s.email =:email and c.id=:id")
    Signer findByEmailAndContractId(@Param("email") String email,@Param("id") String id);
    List<Signer> findByEmailAndTaxCode(String email,String taxCode);
    @Query(value="select s.* from contract c, signer_contract sc, signer s"
    +" where c.contract_id = sc.contract_id and sc.signer_id = s.signer_id"
    +" and c.contract_id =:id and s.signer_id not in (select s1.signer_id from contract c1, signer_contract sc1, signer s1, users u1"
    +" where c1.contract_id = sc1.contract_id and sc1.signer_id = s1.signer_id and u1.user_id=s1.user_id"
    +" and c1.contract_id =:id and u1.accecpt_email_notification is FALSE)",nativeQuery = true)
    List<Signer> findByAcceptEmail(@Param("id") String id);
  
}
