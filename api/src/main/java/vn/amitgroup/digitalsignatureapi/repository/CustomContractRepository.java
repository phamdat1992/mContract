package vn.amitgroup.digitalsignatureapi.repository;


import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.amitgroup.digitalsignatureapi.dto.ContractDto;

public interface CustomContractRepository  {
    Page<ContractDto> search(Map<String,String> map,Pageable page,String email);
}
