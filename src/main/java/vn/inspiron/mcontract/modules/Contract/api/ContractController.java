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
import vn.inspiron.mcontract.modules.Contract.dto.ContractMessageResponse;
import vn.inspiron.mcontract.modules.Contract.dto.ContractResponse;
import vn.inspiron.mcontract.modules.Contract.dto.NewContractDTO;
import vn.inspiron.mcontract.modules.Contract.services.ContractService;
import vn.inspiron.mcontract.modules.Contract.services.NewContractService;
import vn.inspiron.mcontract.modules.Contract.services.PagingContractService;
import vn.inspiron.mcontract.modules.Entity.UserEntity;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
public class ContractController {
    @Autowired
    private PagingContractService pagingContractService;
    @Autowired
    private NewContractService newContractService;
    @Autowired
    private ContractService contractService;

    @PostMapping("/create-contract")
    public ResponseEntity<NewContractDTO> createContract(
            @RequestBody NewContractDTO newContractDTO,
            Authentication authentication
    ) {
        try {
            UserEntity userEntity = ((UserAuth) authentication.getPrincipal()).getUserEntity();
            this.newContractService.createContract(newContractDTO, userEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(newContractDTO);
    }
    
    @GetMapping("/get-list-contract-by-condition")
    public ResponseEntity<MContractResponseBody<List<ContractResponse>>> getListContractByCondition(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                                                    @RequestParam(value = "pageSize", defaultValue = "999999") int pageSize,
                                                                                                    @RequestParam(value = "searchType") ContractSearchType searchType,
                                                                                                    @RequestParam(value = "bookmarkStar", required = false) boolean bookmarkStar,
                                                                                                    Authentication authentication) {
        UserEntity userEntity = ((UserAuth) authentication.getPrincipal()).getUserEntity();
        MContractResponseBody<List<ContractResponse>> responseBody = new MContractResponseBody<>();
        try {
            responseBody = this.pagingContractService.getContractByCondition(userEntity, searchType, pageNumber, pageSize, bookmarkStar);
            return ResponseEntity.ok(responseBody);
        } catch (BadRequest e) {
            responseBody.setMsg(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } catch (Exception e) {
            responseBody.setMsg("Other error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }
    
    @GetMapping("/get-detail-contract-for-user")
    public ResponseEntity<MContractResponseBody<ContractResponse>> getDetailContractForUser(@RequestParam(value = "contractId") String id,
                                                                                            @RequestParam(value = "mail", required = false) String mail,
                                                                                    Authentication authentication) {
        UserEntity userEntity = Objects.nonNull(authentication) ? ((UserAuth) authentication.getPrincipal()).getUserEntity() : null;
        MContractResponseBody<ContractResponse> responseBody = this.contractService.getDetailContractForUser(Long.parseLong(id), Objects.nonNull(userEntity) ? userEntity.getId() : null, mail); // wait Khai supply service decode id
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
    
    @PostMapping("/update-contract-message")
    public ResponseEntity<MContractResponseBody<Boolean>> updateContractMessage(@RequestParam(value = "contractId") String id,
                                                                                @RequestBody ContractMessageResponse contractMessageResponse,
                                                                                Authentication authentication) {
        UserEntity userEntity = Objects.nonNull(authentication) ? ((UserAuth) authentication.getPrincipal()).getUserEntity() : null;
        MContractResponseBody<Boolean> responseBody = new MContractResponseBody<>();
        try {
            responseBody = this.contractService.updateContractMessage(contractMessageResponse, Objects.isNull(userEntity) ? null : userEntity.getId(), Long.parseLong(id)); // wait Khai supply service decode id
            return ResponseEntity.ok(responseBody);
        } catch (BadRequest e) {
            responseBody.setMsg(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } catch (Exception e) {
            responseBody.setMsg("Other error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }
    
    @PatchMapping("/cancel-contract")
    public ResponseEntity<MContractResponseBody<Boolean>> cancelContract(@RequestParam(value = "contractId") String id,
                                                                                Authentication authentication) {
        UserEntity userEntity = ((UserAuth) authentication.getPrincipal()).getUserEntity();
        MContractResponseBody<Boolean> responseBody = new MContractResponseBody<>();
        try {
            responseBody = this.contractService.cancelContractByUser(userEntity.getId(), Long.parseLong(id)); // wait Khai supply service decode id
            return ResponseEntity.ok(responseBody);
        } catch (BadRequest e) {
            responseBody.setData(false);
            responseBody.setMsg(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } catch (Exception e) {
            responseBody.setData(false);
            responseBody.setMsg("Other error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }
    
    @PatchMapping("/cancel-contract-by-guest")
    public ResponseEntity<MContractResponseBody<Boolean>> cancelContract(@RequestParam(value = "contractId") String id,
                                                                         @RequestParam(value = "mail") String mail) {
        MContractResponseBody<Boolean> responseBody = new MContractResponseBody<>();
        try {
            responseBody = this.contractService.cancelContractByGuest(mail, Long.parseLong(id));
            return ResponseEntity.ok(responseBody);
        } catch (BadRequest e) {
            responseBody.setData(false);
            responseBody.setMsg(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } catch (Exception e) {
            responseBody.setData(false);
            responseBody.setMsg("Other error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }
}
