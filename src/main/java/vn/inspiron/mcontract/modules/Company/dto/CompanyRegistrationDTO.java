package vn.inspiron.mcontract.modules.Company.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CompanyRegistrationDTO {

    @NotNull
    private String name;

    @NotNull
    @NotEmpty
    private String mst;

    private String website;

    @NotNull
    private String street;

    @NotNull
    private String ward;

    @NotNull
    private String district;

    @NotNull
    private String city;
}
