package vn.inspiron.mcontract.modules.FileStorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileUploadResponse {

    @JsonProperty("file_id")
    private Long fileId;
}
