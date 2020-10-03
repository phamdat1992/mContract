package vn.inspiron.mcontract.modules.Authentication.services;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Authentication.model.UserAuth;
import vn.inspiron.mcontract.modules.Entity.UserEntity;
import vn.inspiron.mcontract.modules.Repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserAuth loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("Username " + s + " not exist"));
        if (!userEntity.isEnabled()) {
            throw new DisabledException("Email is not verified.");
        }
        return new UserAuth(userEntity);
    }

    public UserAuth loadUserByToken(String token) throws Exception {
        UserEntity userEntity = userRepository.findByToken(token).orElseThrow(() -> new Exception("Invalid token"));
        if (!userEntity.isEnabled()) {
            throw new Exception("Email is not verified.");
        }
        return new UserAuth(userEntity);
    }

    public UserAuth loadUserByID(String userID) throws Exception {
        UserEntity userEntity = userRepository.findByToken(userID).orElseThrow(() -> new Exception("Invalid token"));
        if (!userEntity.isEnabled()) {
            throw new Exception("Email is not verified.");
        }
        return new UserAuth(userEntity);
    }
}
