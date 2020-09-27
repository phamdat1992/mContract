package vn.inspiron.mcontract.modules.Authentication.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationService
{
    private static final int TOKEN_EXPIRATION = 24 * 60 * 60;

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

        if (!userNotExists(userRegistrationDTO.getUsername())) {
            throw new UserExisted();
        }

        Optional<EmailEntity> email = emailRepository.findByEmail(userRegistrationDTO.getEmail());
        EmailEntity newEmail = new EmailEntity();
        // If email not existed, take email from user input
        if (email.isEmpty()) {
            newEmail.setEmail(userRegistrationDTO.getEmail());
            emailRepository.save(newEmail);
        } else { // Email existed, probably the corresponding user does not exist yet
            newEmail.setEmail(email.get().getEmail());
            newEmail.setFkUser(email.get().getFkUser());
        }

        // If this email has no associated user, create user from request.
        // Otherwise, the user with this email already existed
        if (newEmail.getFkUser() == null) {
            UserEntity user = userRepository.save(this.createUser(userRegistrationDTO));

            String randomToken = UUID.randomUUID().toString();

            EmailVerifyTokenEntity emailVerifyTokenEntity = new EmailVerifyTokenEntity();
            emailVerifyTokenEntity.setUser(user);
            emailVerifyTokenEntity.setToken(randomToken);
            emailVerifyTokenEntity.setExpiry(Util.calculateDateFromNow(TOKEN_EXPIRATION));
            emailVerifyTokenEntity.setActive(true);
            this.setToken(emailVerifyTokenEntity);

            this.sendVerificationEmail(newEmail.getEmail(), randomToken);
        }
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

    public void verifyToken(String token) {
        Optional<EmailVerifyTokenEntity> tokenEntity = emailVerifyRepository.findByToken(token);
        if (tokenEntity.isEmpty()) {
            throw new InvalidToken();
        }

        if (!tokenEntity.get().isActive()) {
            throw new TokenExpired();
        }

        java.util.Date now = new java.util.Date();
        if (tokenEntity.get().getExpiry().getTime() - now.getTime() <= 0) {
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
        user.setDateOfRegistration(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setToken(UUID.randomUUID().toString());

        return user;
    }
}
