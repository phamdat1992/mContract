package vn.inspiron.mcontract.modules.Authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.inspiron.mcontract.modules.Entity.MstEntity;

@Repository
public interface MstRepository extends JpaRepository<MstEntity, Long> {
}