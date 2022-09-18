package vn.amitgroup.digitalsignatureapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vn.amitgroup.digitalsignatureapi.entity.TokenIsDestroy;

public interface TokenIsDestroyRepository extends JpaRepository<TokenIsDestroy,Integer> {
    List<TokenIsDestroy> findByToken(String token);
    @Query(value = "DELETE FROM token_destroy t WHERE t.expiration_time <= NOW()",nativeQuery = true)
    @Modifying
    void clear(); 
}
