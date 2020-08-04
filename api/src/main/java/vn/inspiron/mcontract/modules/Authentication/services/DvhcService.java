package vn.inspiron.mcontract.modules.Authentication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Entity.DvhcCityEntity;
import vn.inspiron.mcontract.modules.Entity.DvhcDistrictEntity;
import vn.inspiron.mcontract.modules.Entity.DvhcWardEntity;
import vn.inspiron.mcontract.modules.Repository.DvhcCityRepository;
import vn.inspiron.mcontract.modules.Repository.DvhcDistrictRepository;
import vn.inspiron.mcontract.modules.Repository.DvhcWardRepository;

import java.util.List;

@Service
public class DvhcService {

    @Autowired
    DvhcCityRepository dvhcCityRepository;
    @Autowired
    DvhcDistrictRepository dvhcDistrictRepository;
    @Autowired
    DvhcWardRepository dvhcWardRepository;

    public List<DvhcCityEntity> getAllCities() {
        return dvhcCityRepository.findAll();
    }

    public List<DvhcDistrictEntity> getDistricts(Long ofCity) {
        return dvhcDistrictRepository.findByFkDvhcCity(ofCity);
    }

    public List<DvhcWardEntity> getWards(Long ofDistrict) {
        return dvhcWardRepository.findByFkDvhcDistrict(ofDistrict);
    }
}
