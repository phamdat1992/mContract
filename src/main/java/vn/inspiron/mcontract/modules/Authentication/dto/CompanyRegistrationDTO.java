package vn.inspiron.mcontract.modules.Authentication.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRegistrationDTO {

    @NotNull
    @NotEmpty
    private String mst;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String website;

    @NotNull
    @NotEmpty
    private String street;

    @NotNull
    @NotEmpty
    private String ward;

    @NotNull
    @NotEmpty
    private String district;

    @NotNull
    @NotEmpty
    private String city;
}
