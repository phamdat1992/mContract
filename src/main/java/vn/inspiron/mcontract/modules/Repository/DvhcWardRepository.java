package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.DvhcWardEntity;

import java.util.List;

@Repository
public interface DvhcWardRepository extends JpaRepository<DvhcWardEntity, Long> {

    List<DvhcWardEntity> findByFkDvhcDistrict(Long fkDvhcDistrict);

}
