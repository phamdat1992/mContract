package vn.inspiron.mcontract.modules.Contract.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("user_list")
    private List<NewContractUserDTO> userList;

    @NotNull
    @NotEmpty
    @JsonProperty("file_name")
    private String fileName;

    @NotNull
    @NotEmpty
    @JsonProperty("file_data")
    private String fileData;

    @NotNull
    @NotEmpty
    @JsonProperty("expire_date_signed")
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date expiryDateSigned;
}
