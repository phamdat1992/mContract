package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.ContractEntity;
import vn.inspiron.mcontract.modules.Entity.ContractStatusEntity;

/**
 * @author hoangdd
 * @created 25/08/2020 - 7:41 AM
 **/
@Repository
public interface ContractStatusRepository extends JpaRepository<ContractStatusEntity, Long> {
	ContractStatusEntity getByName(String name);
}
