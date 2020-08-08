package vn.inspiron.mcontract.modules.Authentication.component;

import vn.inspiron.mcontract.modules.Authentication.component.JwtUtils;

public class JwtRefreshToken {
    private String refreshToken;

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public String getSubject()
    {
        return JwtUtils.getSubjectFromToken(this.refreshToken);
    }
}
