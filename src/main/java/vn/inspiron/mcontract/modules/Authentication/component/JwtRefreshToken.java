package vn.inspiron.mcontract.modules.Authentication.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import vn.inspiron.mcontract.modules.Authentication.component.JwtUtils;

@Component
public class JwtRefreshToken {

    @Autowired
    private JwtUtils jwtUtils;

    private String refreshToken;

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public String getSubject()
    {
        return jwtUtils.getSubjectFromToken(this.refreshToken);
    }
}
