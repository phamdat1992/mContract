package vn.amitgroup.digitalsignatureapi.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.amitgroup.digitalsignatureapi.entity.Ward;

import java.util.List;


public interface WardRepository extends JpaRepository<Ward, Integer>{
    Page<Ward> findAll(Pageable pageable);
    Page<Ward> findByDistrictId(int districtId, Pageable pageable);
    List<Ward> findByDistrictId(int districtId);
}
