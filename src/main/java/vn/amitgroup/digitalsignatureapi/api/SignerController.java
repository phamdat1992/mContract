package vn.amitgroup.digitalsignatureapi.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import vn.amitgroup.digitalsignatureapi.dto.SignerDto;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.service.SignerService;
import vn.amitgroup.digitalsignatureapi.utils.SignerUtil;

@RestController
@RequestMapping("/api/signers")
public class SignerController {
    @Autowired
    private SignerService signerService;
    @Operation(summary = "get detail signer")
    @GetMapping()
    public ResponseEntity<BaseResponse<SignerDto>> getContractDetailForSigner(HttpServletRequest request) {
        return ResponseEntity.ok()
                .body(new BaseResponse<>(signerService.getSigner(SignerUtil.getToken(request)), HttpStatus.OK.value()));
    }
}
