package vn.inspiron.mcontract.modules.Contract.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationResponseDTO;
import vn.inspiron.mcontract.modules.Contract.dto.NewContractDTO;
import vn.inspiron.mcontract.modules.Contract.services.ContractService;

@RestController
public class ContractController {
    @Autowired
    private ContractService contractService;

    @PostMapping("/create-contract")
    public ResponseEntity<NewContractDTO> createContract(@RequestBody NewContractDTO newContractDTO) {
        contractService.createContract(newContractDTO);
        return ResponseEntity.ok(newContractDTO);
    }
}
