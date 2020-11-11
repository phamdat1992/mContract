package vn.inspiron.mcontract.modules.Contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewContractDTO {
    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    @NotEmpty
    private List<NewContractUserDTO> userList;

    @NotNull
    @NotEmpty
    private String fileName;

    @NotNull
    @NotEmpty
    private String fileData;

    @NotNull
    @NotEmpty
    private Date expiryDateSigned;
}
