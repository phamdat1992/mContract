package vn.inspiron.mcontract.modules.Authenticate.model.jwt;

public class JwtTokenRequest {
    private String username;
    private String password;

    public JwtTokenRequest()
    {
    }

    public JwtTokenRequest(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "JwtTokenRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
