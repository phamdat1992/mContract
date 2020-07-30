package vn.inspiron.mcontract.modules.Authentication.component;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEvent;
import vn.inspiron.mcontract.modules.Entity.UserEntity;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String toEmail;
    private String token;

    /**
     * Create a new {@code ApplicationEvent}.
     */
    public OnRegistrationCompleteEvent(String toEmail, String token) {
        super(toEmail);
        this.toEmail = toEmail;
        this.token = token;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getToken() {
        return token;
    }
}
