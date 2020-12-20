package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.CompanyEntity;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
	CompanyEntity getFirstByFkMst(Long mstId);
}
