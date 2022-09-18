package vn.amitgroup.digitalsignatureapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.amitgroup.digitalsignatureapi.dto.WardDto;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.Ward;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.WardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WardService {
    @Autowired
    WardRepository wardRepository;

    public BaseResponse<List<WardDto>> getAll(Pageable pageable){
        BaseResponse<List<WardDto>> response = new BaseResponse<>();
        ModelMapper modelMapper = new ModelMapper();
        if (pageable != null){
            Page<WardDto> wardPage = wardRepository.findAll(pageable)
                    .map(ward -> modelMapper.map(ward, WardDto.class));

            response.setPageIndex(wardPage.getNumber());
            response.setTotal(wardPage.getTotalElements());
            response.setTotalPage(wardPage.getTotalPages());
            response.setData(wardPage.getContent());
        }else{
            List<WardDto> wards = wardRepository.findAll()
                .stream()
                .map(ward -> modelMapper.map(ward, WardDto.class))
                .collect(Collectors.toList());
            response.setData(wards);
        }
        return response;
    }

    public BaseResponse<WardDto> getDetail(int provinceId) throws ApiException {
        BaseResponse<WardDto> response = new BaseResponse<>();
        ModelMapper modelMapper = new ModelMapper();
        Optional<Ward> ward = wardRepository.findById(provinceId);
        if (!ward.isPresent()){
            throw ErrorCodeException.NullException();
        }
        WardDto wardDto =  modelMapper.map(ward.get(), WardDto.class);
        response.setData(wardDto);
        return response;
    }
}
