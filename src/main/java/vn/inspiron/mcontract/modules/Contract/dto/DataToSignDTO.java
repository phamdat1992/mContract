package vn.inspiron.mcontract.modules.Contract.dto;

import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataToSignDTO {

    @NotNull
    private Long fileId;
    @NotNull
    private String signingCertificate;
    @NotNull
    private List<String> certificateChain;
    @NotNull
    private EncryptionAlgorithm encryptionAlgorithm;

}
