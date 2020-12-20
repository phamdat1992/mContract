package vn.inspiron.mcontract.modules.Company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
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
