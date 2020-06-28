package vn.inspiron.mcontract.modules.Authentication.model;

import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserDataService
{
    private UserAppRepository userAppRepository;

    public UserDataService(UserAppRepository userAppRepository)
    {
        System.out.println("in search");
        this.userAppRepository = userAppRepository;
    }

    public Optional<User> getUser(Principal principal)
    {
        System.out.println("in search");
        return userAppRepository.findByUsername(principal.getName());
    }
}
