package vn.amitgroup.digitalsignatureapi.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ContractFrom {
    @NotBlank
    private String title;
    @NotBlank
	private String content;
    @NotNull
    private Integer numberOFExpirationDate;
    private MultipartFile file;
    @Valid
    private List<SignerForm> signers;
    // @NotNull
    // private Long fileId;
    // @NotBlank
    // private String fileName;
}
