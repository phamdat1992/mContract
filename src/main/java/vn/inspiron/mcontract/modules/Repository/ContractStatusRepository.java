package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.inspiron.mcontract.modules.Entity.ContractEntity;
import vn.inspiron.mcontract.modules.Entity.ContractStatusEntity;

/**
 * @author hoangdd
 * @created 25/08/2020 - 7:41 AM
 **/
public interface ContractStatusRepository extends JpaRepository<ContractStatusEntity, Long> {
	ContractStatusEntity getByName(String name);
}
