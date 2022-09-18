package vn.amitgroup.digitalsignatureapi.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SignForSignerForm {
    @NotBlank
    private String pemCert;
    @NotBlank
    private String signed;
    private List<String> base64CertificateChain;
    private String fileName;
}
