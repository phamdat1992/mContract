package vn.inspiron.mcontract.modules.Company.api;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.inspiron.mcontract.modules.Company.dto.CompanyRegistrationDTO;
import vn.inspiron.mcontract.modules.Company.dto.CompanyRegistrationResponse;
import vn.inspiron.mcontract.modules.Company.services.CompanyInfoService;
import vn.inspiron.mcontract.modules.Entity.CompanyEntity;
import vn.inspiron.mcontract.modules.Entity.MstEntity;
import vn.inspiron.mcontract.modules.Entity.UserEntity;
import vn.inspiron.mcontract.modules.Repository.MstRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
public class CompanyInfoController {

    @Autowired
    CompanyInfoService companyInfoService;

    @PostMapping("/company/register")
    public ResponseEntity<CompanyRegistrationResponse> registerCompany(CompanyRegistrationDTO companyRegistrationDTO) {

        CompanyEntity companyEntity = companyInfoService.register(companyRegistrationDTO);
        CompanyRegistrationResponse response = new CompanyRegistrationResponse();
        BeanUtils.copyProperties(companyRegistrationDTO, response);
        response.setId(companyEntity.getId());
        return ResponseEntity.ok(response);
    }

}
