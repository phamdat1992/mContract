package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.amitgroup.digitalsignatureapi.entity.District;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    Page<District> findAll(Pageable pageable);
    Page<District> findByProvinceId(int provinceId, Pageable pageable);
    List<District> findByProvinceId(int provinceId);

}
