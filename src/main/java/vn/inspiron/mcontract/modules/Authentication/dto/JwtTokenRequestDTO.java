package vn.inspiron.mcontract.modules.Authentication.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenRequestDTO {
    private String username;
    private String password;
}
