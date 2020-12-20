package vn.inspiron.mcontract.modules.Authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AccountRequestDTO {
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String username;
}
