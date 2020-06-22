package vn.inspiron.mcontract.modules.Authenticate.model.jwt;

import vn.inspiron.mcontract.modules.Authenticate.helper.JwtHelper;

public class JwtRefreshToken {
    private String refreshToken;

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public String getSubject()
    {
        return JwtHelper.getSubjectFromToken(this.refreshToken);
    }
}
