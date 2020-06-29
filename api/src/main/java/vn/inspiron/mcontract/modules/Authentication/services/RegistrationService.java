package vn.inspiron.mcontract.modules.Authentication.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationDTO;
import vn.inspiron.mcontract.modules.Repository.*;
import vn.inspiron.mcontract.modules.Entity.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
public class RegistrationService
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private MstRepository mstRepository;
    @Autowired
    private CompanyUserRepository companyUserRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(UserRegistrationDTO userRegistrationDTO) throws Exception {
        if (userNotExists(userRegistrationDTO.getUsername())) {
            Optional<EmailEntity> email = emailRepository.findByEmail(userRegistrationDTO.getEmail());
            EmailEntity newEmail = new EmailEntity();
            if (email.isEmpty()) {
                newEmail.setEmail(userRegistrationDTO.getEmail());
                emailRepository.save(newEmail);
            } else {
                newEmail.setEmail(email.get().getEmail());
                newEmail.setFkUser(email.get().getFkUser());
            }

            if (newEmail.getFkUser() == null) {
                UserEntity user = userRepository.save(createUser(userRegistrationDTO));
                CompanyEntity company = companyRepository.save(createCompany(userRegistrationDTO));
                mstRepository.save(createMst(userRegistrationDTO, company));
                CompanyUserEntity companyUser = new CompanyUserEntity();
                companyUser.setFkCompany(company.getId());
                companyUser.setFkUser(user.getId());
                companyUser.setFkCompanyUserRole(1L);

                return;
            }
        }

        throw new Exception("User exist");
    }

    private boolean userNotExists(@NotNull @NotEmpty String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        System.out.println(optionalUser);
        System.out.println(optionalUser.isEmpty());

        return optionalUser.isEmpty();
    }

    private UserEntity createUser(UserRegistrationDTO userRegistrationDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        return user;
    }

    private MstEntity createMst(UserRegistrationDTO userRegistrationDTO, CompanyEntity company) {
        MstEntity mst = new MstEntity();
        BeanUtils.copyProperties(userRegistrationDTO, mst);
        mst.setFkCompany(company.getId());

        return mst;
    }

    private CompanyEntity createCompany(UserRegistrationDTO userRegistrationDTO) {
        CompanyEntity company = new CompanyEntity();
        BeanUtils.copyProperties(userRegistrationDTO, company);

        return company;
    }
}
