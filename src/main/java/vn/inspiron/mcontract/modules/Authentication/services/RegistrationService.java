package vn.inspiron.mcontract.modules.Authentication.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Authentication.component.OnRegistrationCompleteEvent;
import vn.inspiron.mcontract.modules.Authentication.dto.AccountRequestDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationDTO;
import vn.inspiron.mcontract.modules.Common.util.Util;
import vn.inspiron.mcontract.modules.Exceptions.InvalidToken;
import vn.inspiron.mcontract.modules.Exceptions.TokenExpired;
import vn.inspiron.mcontract.modules.Exceptions.UserExisted;
import vn.inspiron.mcontract.modules.Repository.*;
import vn.inspiron.mcontract.modules.Entity.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class RegistrationService
{
    final String EXIST_EMAIL = "The email is taken";
    final String EXIST_USERNAME = "The username is taken";

    @Value("${verify-token-expiration-in-second}")
    private String tokenExpiration;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    EmailVerifyRepository emailVerifyRepository;

    public void register(UserRegistrationDTO userRegistrationDTO) throws Exception {

        UserEntity user = this.registerNewUser(userRegistrationDTO);
        EmailEntity newEmail = this.registerNewEmail(user, userRegistrationDTO.getEmail());
        String randomToken = this.generateVerifyToken(user);
        this.sendVerificationEmail(newEmail.getEmail(), randomToken);
    }

    public String generateVerifyToken(UserEntity user) {
        String randomToken = UUID.randomUUID().toString();

        EmailVerifyTokenEntity emailVerifyTokenEntity = new EmailVerifyTokenEntity();
        emailVerifyTokenEntity.setUser(user);
        emailVerifyTokenEntity.setToken(randomToken);
        emailVerifyTokenEntity.setExpiry(Util.calculateDateFromNow(Integer.parseInt(this.tokenExpiration)));
        emailVerifyTokenEntity.setActive(true);
        this.setToken(emailVerifyTokenEntity);

        return randomToken;
    }

    public UserEntity registerNewUser(UserRegistrationDTO userRegistrationDTO) throws Exception {
        Optional<UserEntity> optionalUser = this.userRepository.findByUsername(userRegistrationDTO.getUsername());
        UserEntity user = this.createUser(userRegistrationDTO);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().isEnabled() || this.isActiveVerifyToken(optionalUser.get().getId())) {
                throw new UserExisted();
            }

            user.setId(optionalUser.get().getId());
        }

        return this.userRepository.save(user);
    }

    public EmailEntity registerNewEmail(UserEntity user, String email) throws Exception {
        Optional<EmailEntity> emailEntity = this.emailRepository.findByEmail(email);
        EmailEntity newEmail = new EmailEntity();
        if (emailEntity.isPresent()) {
            newEmail.setId(emailEntity.get().getId());
            if (!Objects.equals(emailEntity.get().getFkUser(), user.getId())) {
                Optional<UserEntity> optionalUser = this.userRepository.findById(emailEntity.get().getFkUser());
                if (optionalUser.isPresent()) {
                    if (optionalUser.get().isEnabled() || this.isActiveVerifyToken(optionalUser.get().getId())) {
                        throw new UserExisted();
                    }
                }
            }
        }

        newEmail.setEmail(email);
        newEmail.setFkUser(user.getId());
        this.emailRepository.save(newEmail);

        return newEmail;
    }

    public ArrayList<String> checkAccount(AccountRequestDTO account) {
        ArrayList<String> message = new ArrayList<String>();

        Optional<EmailEntity> emailEntity = this.emailRepository.findByEmail(account.getEmail());
        if (emailEntity.isPresent()) {
            Optional<UserEntity> optionalUser = userRepository.findById(emailEntity.get().getFkUser());
            if (optionalUser.isPresent()) {
                if (optionalUser.get().isEnabled() || this.isActiveVerifyToken(optionalUser.get().getId())) {
                    if (account.getUsername().equals(optionalUser.get().getUsername())) {
                        message.add(this.EXIST_EMAIL);
                        message.add(this.EXIST_USERNAME);
                    } else {
                        message.add(this.EXIST_EMAIL);
                    }
                }
            }
        } else {
            Optional<UserEntity> optionalUser = userRepository.findByUsername(account.getUsername());
            if (optionalUser.isPresent()) {
                if (optionalUser.get().isEnabled() || this.isActiveVerifyToken(optionalUser.get().getId())) {
                    message.add(this.EXIST_USERNAME);
                }
            }
        }

        return message;
    }

    public void sendVerificationEmail(String email, String token) {
        // Send verification email
        try {
            this.eventPublisher.publishEvent(new OnRegistrationCompleteEvent(email, token));
        } catch (RuntimeException e) {
            // TODO: Handle email not sent exception
            e.printStackTrace();
        }
    }

    public void setToken(EmailVerifyTokenEntity tokenEntity) {
        emailVerifyRepository.save(tokenEntity);
    }

    public boolean isActiveVerifyToken(Long userId) {
        Optional<EmailVerifyTokenEntity> tokenEntity = emailVerifyRepository.getActiveTokenByUserId(userId);

        if (tokenEntity.isEmpty()) {
            return true;
        }

        if (!tokenEntity.get().isActive()) {
            return false;
        }

        if (this.isExpiredTime(tokenEntity.get().getExpiry())) {
            if (tokenEntity.get().isActive()) {
                tokenEntity.get().setActive(false);
                this.emailVerifyRepository.save(tokenEntity.get());
            }
            return false;
        }

        return true;
    }

    public boolean isExpiredTime(Date time) {
        java.util.Date now = new java.util.Date();
        return (time.getTime() - now.getTime() <= 0);
    }

    public void verifyToken(String token) {
        Optional<EmailVerifyTokenEntity> tokenEntity = emailVerifyRepository.findByToken(token);
        if (tokenEntity.isEmpty()) {
            throw new InvalidToken();
        }

        if (!tokenEntity.get().isActive()) {
            throw new TokenExpired();
        }

        if (this.isExpiredTime(tokenEntity.get().getExpiry())) {
            throw new TokenExpired();
        }

        tokenEntity.get().setActive(false);
        this.emailVerifyRepository.save(tokenEntity.get());

        UserEntity user = tokenEntity.get().getUser();
        user.setEnabled(true);
        this.userRepository.save(user);
    }

    private boolean userNotExists(@NotNull @NotEmpty String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        return optionalUser.isEmpty();
    }

    private UserEntity createUser(UserRegistrationDTO userRegistrationDTO) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(userRegistrationDTO, user);
        user.setDateOfRegistration(new java.sql.Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setToken(UUID.randomUUID().toString());

        return user;
    }
}
