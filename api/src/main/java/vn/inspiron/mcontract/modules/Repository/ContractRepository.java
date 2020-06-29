package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.CompanyUserEntity;
import vn.inspiron.mcontract.modules.Entity.ContractEntity;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
}