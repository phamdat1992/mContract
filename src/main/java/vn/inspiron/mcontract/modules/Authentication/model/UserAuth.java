package vn.inspiron.mcontract.modules.Authentication.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import vn.inspiron.mcontract.modules.Entity.UserEntity;

import java.util.Collection;
import java.util.Collections;

public class UserAuth extends User {

    private UserEntity userEntity;

    public UserAuth(UserEntity userEntity) {
        super(userEntity.getUsername(), userEntity.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ADMIN")));
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
}
