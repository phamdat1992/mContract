package vn.amitgroup.digitalsignatureapi.service;

import vn.amitgroup.digitalsignatureapi.dto.CompanyAddForm;
import vn.amitgroup.digitalsignatureapi.dto.CompanyDto;
import vn.amitgroup.digitalsignatureapi.dto.CompanyUpdateForm;

public interface CompanyService {
    CompanyDto update(CompanyUpdateForm company,String email);
    CompanyDto add(CompanyAddForm company,String email);
    CompanyDto getByUser(String email);
}
