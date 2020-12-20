package vn.inspiron.mcontract.modules.Contract.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignDocumentResponse {

    @JsonProperty("file_id")
    private Long fileId;
    private String filename;
}
