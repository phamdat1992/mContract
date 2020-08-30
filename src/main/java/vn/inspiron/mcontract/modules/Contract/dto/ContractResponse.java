package vn.inspiron.mcontract.modules.Contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.inspiron.mcontract.modules.User.dto.UserResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractResponse {
    private String id;
    private String companyName;
    private String companyAddress;
    private String title;
    private String description;
    private String shortDescription;
    private String fileName;
    private String latestContentMessage;
    private Boolean bookmarkStar;
    private String urlFile;
    
    private List<UserResponse> userResponse;
    private List<ContractMessageResponse> contractMessageResponses;
}
