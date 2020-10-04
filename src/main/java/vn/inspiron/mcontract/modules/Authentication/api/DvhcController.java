package vn.inspiron.mcontract.modules.Authentication.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.inspiron.mcontract.modules.Authentication.dto.DvhcDTO;
import vn.inspiron.mcontract.modules.Authentication.services.DvhcService;
import vn.inspiron.mcontract.modules.Entity.DvhcCityEntity;
import vn.inspiron.mcontract.modules.Entity.DvhcDistrictEntity;
import vn.inspiron.mcontract.modules.Entity.DvhcWardEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class DvhcController {

    @Autowired
    private DvhcService dvhcService;

    @GetMapping("/cities")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<DvhcDTO>> getCities() throws Exception {
        List<DvhcCityEntity> entities = dvhcService.getAllCities();
        List<DvhcDTO> cities = new ArrayList<DvhcDTO>();
        for (DvhcCityEntity entity : entities) {
            cities.add(new DvhcDTO(entity.getId(), entity.getPrefix() + " " + entity.getName()));
        }
        return Collections.singletonMap("cities", cities);
    }

    @GetMapping("/cities/{cityId}/districts")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<DvhcDTO>> getDistricts(@PathVariable Long cityId) throws Exception {
        List<DvhcDistrictEntity> entities = this.dvhcService.getDistricts(cityId);
        List<DvhcDTO> districts = new ArrayList<DvhcDTO>();
        for (DvhcDistrictEntity entity : entities) {
            districts.add(new DvhcDTO(entity.getId(), entity.getPrefix() + " " + entity.getName()));
        }
        return Collections.singletonMap("districts", districts);
    }

    @GetMapping("/districts/{districtId}/wards")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<DvhcDTO>> getWards(@PathVariable Long districtId) throws Exception {
        List<DvhcWardEntity> entities = dvhcService.getWards(districtId);
        List<DvhcDTO> wards = new ArrayList<DvhcDTO>();
        for (DvhcWardEntity entity : entities) {
            wards.add(new DvhcDTO(entity.getId(), entity.getPrefix() + " " + entity.getName()));
        }
        return Collections.singletonMap("wards", wards);
    }

}
