package vn.amitgroup.digitalsignatureapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.amitgroup.digitalsignatureapi.dto.DistrictDto;
import vn.amitgroup.digitalsignatureapi.dto.ProvinceDto;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.Province;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.DistrictRepository;
import vn.amitgroup.digitalsignatureapi.repository.ProvinceRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProvinceService {
    @Autowired
    ProvinceRepository provinceRepository;
    @Autowired
    DistrictRepository districtRepository;


    public BaseResponse<List<ProvinceDto>> getAll(Pageable pageable){
        BaseResponse<List<ProvinceDto>> response = new BaseResponse<>();
        ModelMapper modelMapper = new ModelMapper();
        if (pageable != null){
            Page<ProvinceDto> provinceDtoPage = provinceRepository.findAll(pageable)
                    .map(province -> modelMapper.map(province, ProvinceDto.class));

            response.setPageIndex(provinceDtoPage.getNumber());
            response.setTotal(provinceDtoPage.getTotalElements());
            response.setTotalPage(provinceDtoPage.getTotalPages());
            response.setData(provinceDtoPage.getContent());
        }else{
            List<ProvinceDto> provinces = provinceRepository.findAll()
                    .stream()
                    .map(province -> modelMapper.map(province, ProvinceDto.class))
                    .collect(Collectors.toList());
            response.setData(provinces);
        }
        return response;
    }

    public BaseResponse<ProvinceDto> getDetail(int provinceId) throws ApiException {
        BaseResponse<ProvinceDto> response = new BaseResponse<>();
        ModelMapper modelMapper = new ModelMapper();
        Optional<Province> province = provinceRepository.findById(provinceId);
        if (!province.isPresent()){
            throw ErrorCodeException.NullException();
        }
        ProvinceDto provinceDto = modelMapper.map(province.get(), ProvinceDto.class);
        response.setData(provinceDto);
        return response;
    }

    public BaseResponse<List<DistrictDto>> getDistricts(int provinceId, Pageable pageable) throws ApiException {
        BaseResponse<List<DistrictDto>> response = new BaseResponse<>();
        ModelMapper modelMapper = new ModelMapper();
        Optional<Province> province = provinceRepository.findById(provinceId);
        if (!province.isPresent()){
            throw ErrorCodeException.NullException();
        }

        if (pageable != null){
            Page<DistrictDto> districtPage = districtRepository.findByProvinceId(provinceId, pageable)
                    .map(district -> modelMapper.map(district, DistrictDto.class));

            response.setPageIndex(districtPage.getNumber());
            response.setTotal(districtPage.getTotalElements());
            response.setTotalPage(districtPage.getTotalPages());
            response.setData(districtPage.getContent());
        }else{
            List<DistrictDto> districts = districtRepository.findByProvinceId(provinceId)
                    .stream()
                    .map(district -> modelMapper.map(district, DistrictDto.class))
                    .collect(Collectors.toList());
            response.setData(districts);
        }
        return response;
    }
}
