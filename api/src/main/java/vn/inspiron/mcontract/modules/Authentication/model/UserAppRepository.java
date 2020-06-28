package vn.inspiron.mcontract.modules.Authentication.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAppRepository extends JpaRepository<User, Long>
{
    Optional<User> findByUsername(String username);
}
