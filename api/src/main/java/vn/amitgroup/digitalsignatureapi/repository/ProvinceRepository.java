package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.amitgroup.digitalsignatureapi.entity.Province;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    Page<Province> findAll(Pageable pageable);
}
