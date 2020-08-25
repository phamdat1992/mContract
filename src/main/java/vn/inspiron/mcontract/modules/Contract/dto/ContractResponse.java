package vn.inspiron.mcontract.modules.Contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractResponse {
    private String id;
    private String companyName;
    private String companyAddress;
    private String title;
    private String description;
    private String fileName;
    
}
