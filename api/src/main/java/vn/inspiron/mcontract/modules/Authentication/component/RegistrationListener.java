package vn.inspiron.mcontract.modules.Authentication.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import vn.inspiron.mcontract.modules.Authentication.services.RegistrationService;
import vn.inspiron.mcontract.modules.Common.util.Util;
import vn.inspiron.mcontract.modules.Entity.EmailVerifyTokenEntity;
import vn.inspiron.mcontract.modules.Entity.UserEntity;
import vn.inspiron.mcontract.modules.Repository.EmailVerifyRepository;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        String toEmail = event.getToEmail();

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject("Please complete signing up at mContract!");
        // TODO: Handle magic URL here
        email.setText("Thank you for signing up at mContract.\nPlease visit this link to complete your registration: \n" +
                "http://localhost:9293/register/verify?token=" + event.getToken());
        mailSender.send(email);
        System.out.println("Verification Email sent");
    }

}