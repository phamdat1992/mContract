package vn.inspiron.mcontract.modules.Authentication.component;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEvent;
import vn.inspiron.mcontract.modules.Entity.UserEntity;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String toEmail;
    private UserEntity user;

    /**
     * Create a new {@code ApplicationEvent}.
     */
    public OnRegistrationCompleteEvent(String toEmail, UserEntity user) {
        super(toEmail);
        this.toEmail = toEmail;
        this.user = user;
    }

    public String getToEmail() {

        return toEmail;
    }

    public UserEntity getUser() {
        return user;
    }
}
