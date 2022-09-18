package vn.amitgroup.digitalsignatureapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.amitgroup.digitalsignatureapi.entity.UserOtp;
@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp, Integer> {
    @Query(value = "SELECT * FROM user_otp WHERE key = :key AND NOW() <= expiration_time ORDER BY expiration_time DESC", nativeQuery = true)
    List<UserOtp> findCustom(@Param("key") String key);

    Long deleteByKey(String key);
}
