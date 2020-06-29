package vn.inspiron.mcontract.modules.Authentication.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String email;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String mst;
    @NotNull
    @NotEmpty
    private String companyName;
    @NotNull
    @NotEmpty
    private String companyAddress;
}
