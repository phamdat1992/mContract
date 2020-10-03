package vn.inspiron.mcontract.modules.Authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyResetPasswordDTO {
    @NotNull
    @NotEmpty
    private String token;

    @NotNull
    @NotEmpty
    private String code;

    @NotNull
    @NotEmpty
    private String password;
}
