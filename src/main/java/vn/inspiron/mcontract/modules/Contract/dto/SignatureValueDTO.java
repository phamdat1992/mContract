package vn.inspiron.mcontract.modules.Contract.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SignatureValueDTO extends DataToSignDTO {

    @NotNull
    @JsonProperty("signature_value")
    private String signatureValue;

}