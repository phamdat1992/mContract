package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.amitgroup.digitalsignatureapi.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Company findByUser_Email(String email);
    Company findByTaxCodeAndUser_Email(String taxCode,String email);
}
