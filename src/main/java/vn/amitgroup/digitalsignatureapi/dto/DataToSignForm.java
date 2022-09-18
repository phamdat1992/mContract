package vn.amitgroup.digitalsignatureapi.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DataToSignForm {
    @NotNull
    private String contractId;
    @NotBlank
    private String pemCert;
    private List<String> base64CertificateChain;
}
