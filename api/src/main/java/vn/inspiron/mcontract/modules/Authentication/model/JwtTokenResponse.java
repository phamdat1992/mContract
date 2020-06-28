package vn.inspiron.mcontract.modules.Authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class JwtTokenResponse {
    private String token;

    public JwtTokenResponse(String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }
}
