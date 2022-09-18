package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.amitgroup.digitalsignatureapi.entity.S3File;

import java.util.List;

public interface S3FileRepository extends JpaRepository<S3File, Long> {
    Page<S3File> findAll(Pageable pageable);
    List<S3File> findByIdIn(List<Long> s3fileIdList);
}
