package vn.inspiron.mcontract.modules.Authentication.model;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private UserAppRepository userAppRepository;

    public UserDetailsServiceImpl(UserAppRepository userAppRepository)
    {
        this.userAppRepository = userAppRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        System.out.println("in search");
        UserDetails userDetails = userAppRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s + "No exist"));
        System.out.println("xxxxxxxxxxxxxx");
        if (!userDetails.isEnabled())
        {
            throw new DisabledException("User is not enabled");
        }

        System.out.println(userDetails.getPassword());
        return new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }
}
