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
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationDTO;
import vn.inspiron.mcontract.modules.Common.util.Util;
import vn.inspiron.mcontract.modules.Exceptions.InvalidToken;
import vn.inspiron.mcontract.modules.Exceptions.TokenExpired;
import vn.inspiron.mcontract.modules.Exceptions.UserExisted;
import vn.inspiron.mcontract.modules.Repository.*;
import vn.inspiron.mcontract.modules.Entity.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationService
{
    @Value("${verify-token-expiration}")
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
        Optional<UserEntity> optionalUser = userRepository.findByUsername(userRegistrationDTO.getUsername());
        UserEntity user = this.createUser(userRegistrationDTO);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().isEnabled() || this.isActiveVerifyToken(optionalUser.get().getId())) {
                throw new UserExisted();
            }

            user.setId(optionalUser.get().getId());
        }

        return userRepository.save(user);
    }

    public EmailEntity registerNewEmail(UserEntity user, String email) {
        Optional<EmailEntity> emailEntity = this.emailRepository.findByEmail(email);
        EmailEntity newEmail = new EmailEntity();
        // If email not existed, take email from user input
        if (emailEntity.isEmpty()) {
            newEmail.setEmail(email);
            newEmail.setFkUser(user.getId());
            this.emailRepository.save(newEmail);
        } else { // Email existed, probably the corresponding user does not exist yet
            newEmail.setEmail(emailEntity.get().getEmail());
            newEmail.setId(emailEntity.get().getId());
            newEmail.setFkUser(emailEntity.get().getFkUser());

            if (!Objects.equals(newEmail.getFkUser(), user.getId())) {
                newEmail.setFkUser(user.getId());
                this.emailRepository.save(newEmail);
            }
        }

        return newEmail;
    }

    public void sendVerificationEmail(String email, String token) {
        // Send verification email
        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(email, token));
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
