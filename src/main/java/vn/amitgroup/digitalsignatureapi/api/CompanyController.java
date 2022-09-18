package vn.amitgroup.digitalsignatureapi.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.amitgroup.digitalsignatureapi.dto.CompanyAddForm;
import vn.amitgroup.digitalsignatureapi.dto.CompanyDto;
import vn.amitgroup.digitalsignatureapi.dto.CompanyUpdateForm;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.service.CompanyService;
import vn.amitgroup.digitalsignatureapi.utils.UserUtil;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @GetMapping()
    public ResponseEntity<BaseResponse<CompanyDto>> add(){
        return ResponseEntity.ok().body(new BaseResponse<>(companyService.getByUser(UserUtil.email()),HttpStatus.OK.value()) );
    }
    @PostMapping()
    public ResponseEntity<BaseResponse<CompanyDto>> add(@RequestBody @Valid CompanyAddForm company){
        return ResponseEntity.ok().body(new BaseResponse<>(companyService.add(company,UserUtil.email()),HttpStatus.OK.value()) );
    }
    @PutMapping()
    public ResponseEntity<BaseResponse<CompanyDto>> update(@RequestBody @Valid CompanyUpdateForm company){
        return ResponseEntity.ok().body(new BaseResponse<>(companyService.update(company,UserUtil.email()),HttpStatus.OK.value()) );
    }
}
