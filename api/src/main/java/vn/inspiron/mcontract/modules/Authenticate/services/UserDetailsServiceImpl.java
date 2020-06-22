package vn.inspiron.mcontract.modules.Authenticate.services;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Authenticate.daos.UserAppRepository;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserAppRepository userAppRepository;

    public UserDetailsServiceImpl(UserAppRepository userAppRepository)
    {
        this.userAppRepository = userAppRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        //UserDetails userDetails = userAppRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s + "No exist"));
/*
        if (!userDetails.isEnabled())
        {
            throw new DisabledException("User is not enabled");
        }

        return new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

 */
        return new User("dat.pham", "testtest", Collections.singleton(new SimpleGrantedAuthority("dcm")));
    }
}
