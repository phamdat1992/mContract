package vn.inspiron.mcontract.modules.Authentication.component;

import org.springframework.context.ApplicationEvent;

public class OnResetPasswordEvent extends ApplicationEvent {

    private String toEmail;
    private String token;

    /**
     * Create a new {@code ApplicationEvent}.
     */
    public OnResetPasswordEvent(String toEmail, String toToken) {
        super(toEmail);
        this.toEmail = toEmail;
        this.token = toToken;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getToken() {
        return token;
    }
}
