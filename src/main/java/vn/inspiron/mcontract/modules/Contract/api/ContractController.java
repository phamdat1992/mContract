package vn.inspiron.mcontract.modules.Contract.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.inspiron.mcontract.modules.Authentication.model.UserAuth;
import vn.inspiron.mcontract.modules.Common.data.type.ContractSearchType;
import vn.inspiron.mcontract.modules.Common.util.MContractResponseBody;
import vn.inspiron.mcontract.modules.Contract.dto.ContractResponse;
import vn.inspiron.mcontract.modules.Contract.dto.NewContractDTO;
import vn.inspiron.mcontract.modules.Contract.services.ContractService;
import vn.inspiron.mcontract.modules.Entity.UserEntity;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;

import java.util.List;

@RestController
@Slf4j
public class ContractController {
    @Autowired
    private ContractService contractService;

    @PostMapping("/create-contract")
    public ResponseEntity<NewContractDTO> createContract(@RequestBody NewContractDTO newContractDTO) {
        contractService.createContract(newContractDTO);
        return ResponseEntity.ok(newContractDTO);
    }
    
    @GetMapping("/get-list-contract-by-condition")
    public ResponseEntity<MContractResponseBody<List<ContractResponse>>> getListContractByCondition(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                               @RequestParam(value = "pageSize", defaultValue = "999999") int pageSize,
                                                                               @RequestParam(value = "searchType") ContractSearchType searchType,
                                                                               @RequestParam(value = "bookmarkStar", required = false) boolean bookmarkStar,
                                                                               Authentication authentication) {
        UserEntity userEntity = ((UserAuth) authentication.getPrincipal()).getUserEntity();
        MContractResponseBody<List<ContractResponse>> responseBody = contractService.getContractByCondition(userEntity, searchType, pageNumber, pageSize, bookmarkStar);
        try {
            return ResponseEntity.ok(responseBody);
        } catch (BadRequest e) {
            responseBody.setMsg(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } catch (Exception e) {
            responseBody.setMsg("Other error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
        
    }
}
