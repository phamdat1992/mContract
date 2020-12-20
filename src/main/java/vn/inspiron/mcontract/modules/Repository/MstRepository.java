package vn.inspiron.mcontract.modules.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.MstEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MstRepository extends JpaRepository<MstEntity, Long> {
    Optional<MstEntity> findByMst(String mst);
    List<MstEntity> findByMstIn(List<String> msts);
}