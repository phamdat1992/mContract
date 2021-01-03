package vn.inspiron.mcontract.modules.Authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class GooglePojoDTO {
    @NotNull
    @NotEmpty
    private String id;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private boolean verified_email;
}
