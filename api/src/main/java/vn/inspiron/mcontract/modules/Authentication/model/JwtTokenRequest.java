package vn.inspiron.mcontract.modules.Authentication.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class JwtTokenRequest {
    private String username;
    private String password;
}
