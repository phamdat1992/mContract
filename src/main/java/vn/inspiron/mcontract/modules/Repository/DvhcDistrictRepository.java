package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.DvhcDistrictEntity;

import java.util.List;

@Repository
public interface DvhcDistrictRepository extends JpaRepository<DvhcDistrictEntity, Long> {

    List<DvhcDistrictEntity> findByFkDvhcCity(Long fkDvhcCity);

}
