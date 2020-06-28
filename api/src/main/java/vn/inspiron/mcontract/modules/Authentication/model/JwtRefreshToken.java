package vn.inspiron.mcontract.modules.Authentication.model;

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
