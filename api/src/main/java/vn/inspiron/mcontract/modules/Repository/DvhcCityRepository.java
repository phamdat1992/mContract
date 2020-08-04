package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.DvhcCityEntity;

@Repository
public interface DvhcCityRepository extends JpaRepository<DvhcCityEntity, Long> {
}
