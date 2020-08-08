package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.MstEntity;

import java.util.List;

@Repository
public interface MstRepository extends JpaRepository<MstEntity, Long> {
    List<MstEntity> findByMstIn(List<String> msts);
}