package vn.amitgroup.digitalsignatureapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.amitgroup.digitalsignatureapi.dto.DistrictDto;
import vn.amitgroup.digitalsignatureapi.dto.WardDto;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.District;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.DistrictRepository;
import vn.amitgroup.digitalsignatureapi.repository.WardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DistrictService {
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    WardRepository wardRepository;

    public BaseResponse<List<DistrictDto>> getAll(Pageable pageable){
        BaseResponse<List<DistrictDto>> response = new BaseResponse<>();
        ModelMapper modelMapper = new ModelMapper();
        if (pageable != null){
            Page<DistrictDto> districtPage = districtRepository.findAll(pageable)
                    .map(district -> modelMapper.map(district, DistrictDto.class));

            response.setPageIndex(districtPage.getNumber());
            response.setTotal(districtPage.getTotalElements());
            response.setTotalPage(districtPage.getTotalPages());
            response.setData(districtPage.getContent());
        }else{
            List<DistrictDto> districts = districtRepository.findAll()
                    .stream()
                    .map(district -> modelMapper.map(district, DistrictDto.class))
                    .collect(Collectors.toList());
            response.setData(districts);
        }
        return response;
    }

    public BaseResponse<DistrictDto> getDetail(int districtId) throws ApiException {
        BaseResponse<DistrictDto> response = new BaseResponse<>();
        ModelMapper modelMapper = new ModelMapper();
        Optional<District> district = districtRepository.findById(districtId);
        if (!district.isPresent()){
            throw ErrorCodeException.NullException();
        }
        DistrictDto provinceDto = modelMapper.map(district.get(), DistrictDto.class);
        response.setData(provinceDto);
        return response;
    }

    public BaseResponse<List<WardDto>> getWards(int districtId, Pageable pageable) throws ApiException {
        BaseResponse<List<WardDto>> response = new BaseResponse<>();
        ModelMapper modelMapper = new ModelMapper();
        Optional<District> district = districtRepository.findById(districtId);
        if (!district.isPresent()){
            throw ErrorCodeException.NullException();
        }

        if (pageable != null){
            Page<WardDto> wardPage = wardRepository.findByDistrictId(districtId, pageable)
                    .map(ward -> modelMapper.map(ward, WardDto.class));

            response.setPageIndex(wardPage.getNumber());
            response.setTotal(wardPage.getTotalElements());
            response.setTotalPage(wardPage.getTotalPages());
            response.setData(wardPage.getContent());
        }else{
            List<WardDto> wards = wardRepository.findByDistrictId(districtId)
                    .stream()
                    .map(ward -> modelMapper.map(ward, WardDto.class))
                    .collect(Collectors.toList());
            response.setData(wards);
        }
        return response;
    }
}
