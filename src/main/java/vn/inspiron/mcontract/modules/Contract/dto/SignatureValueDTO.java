package vn.inspiron.mcontract.modules.Contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SignatureValueDTO extends DataToSignDTO {

    private String signatureValue;

}