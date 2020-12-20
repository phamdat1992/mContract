package vn.inspiron.mcontract.modules.Contract.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataToSignResponse {

    @JsonProperty("data_to_sign")
    private String dataToSign;
}
