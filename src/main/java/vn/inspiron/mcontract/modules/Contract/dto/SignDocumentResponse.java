package vn.inspiron.mcontract.modules.Contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignDocumentResponse {

    private Long fileId;
    private String filename;
}
