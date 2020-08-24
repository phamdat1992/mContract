package vn.inspiron.mcontract.modules.Contract.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("file_id")
    private Long fileId;
    @NotNull
    @JsonProperty("signing_certificate")
    private String signingCertificate;
    @NotNull
    @JsonProperty("certificate_chain")
    private List<String> certificateChain;
    @NotNull
    @JsonProperty("encryption_algorithm")
    private EncryptionAlgorithm encryptionAlgorithm;

}
