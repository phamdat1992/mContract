package vn.inspiron.mcontract.modules.Authentication.services;

import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Entity.UserEntity;
import vn.inspiron.mcontract.modules.Repository.UserRepository;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserDataService
{
    private UserRepository userRepository;

    public UserDataService(UserRepository userRepository)
    {
        System.out.println("in search");
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getUser(Principal principal)
    {
        System.out.println("in search");
        return userRepository.findByUsername(principal.getName());
    }
}
