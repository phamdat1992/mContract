package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.amitgroup.digitalsignatureapi.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	User findByEmail(String email);
	@Query(value = " select u from User u join u.company c where u.email =:email and c.taxCode <>:taxCode ")
    User findByEmailAndInvalidTaxCode(@Param("email") String email,@Param("taxCode") String taxCode);
}
