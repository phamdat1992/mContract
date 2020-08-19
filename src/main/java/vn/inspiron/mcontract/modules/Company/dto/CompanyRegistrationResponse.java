package vn.inspiron.mcontract.modules.Company.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CompanyRegistrationResponse {
    private Long id;
    private String name;
    private String mst;
    private String website;
    private String street;
    private String ward;
    private String district;
    private String city;
}
