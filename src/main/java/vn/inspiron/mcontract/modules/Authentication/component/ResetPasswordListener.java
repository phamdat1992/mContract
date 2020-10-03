package vn.inspiron.mcontract.modules.Authentication.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

    @Value("${client.url}")
    private String url;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnResetPasswordEvent event) {
        this.confirmResetPassword(event);
    }

    private void confirmResetPassword(OnResetPasswordEvent event) {
        String toEmail = event.getToEmail();

        String token = event.getToken().substring(0, event.getToken().length() - 6);
        String code = event.getToken().substring(event.getToken().length() - 6);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject("Khoi phuc tai khoan");
        // TODO: Handle magic URL here
        email.setText(this.url + "/verify-reset-password?token=" + token +"\n" +
                "code: " + code);
        this.mailSender.send(email);
    }
}
