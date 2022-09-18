package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import vn.amitgroup.digitalsignatureapi.entity.RefreshToken;
import vn.amitgroup.digitalsignatureapi.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
  @Modifying
  int deleteByUser(User user);
}
