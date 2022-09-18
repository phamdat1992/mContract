package vn.amitgroup.digitalsignatureapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.amitgroup.digitalsignatureapi.dto.DistrictDto;
import vn.amitgroup.digitalsignatureapi.dto.ProvinceDto;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.utils.Paginator;
import vn.amitgroup.digitalsignatureapi.service.ProvinceService;

import java.util.List;


@RestController
@RequestMapping("/api/common/provinces")
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;

    @GetMapping()
    public ResponseEntity<BaseResponse<List<ProvinceDto>>> getAll(
            @RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(value = "size", defaultValue = "0") Integer size) {
        Pageable paging = Paginator.GetPageable(currentPage, size);
        return ResponseEntity.ok().body(provinceService.getAll(paging));
    }
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProvinceDto>> getDetailProvince(
            @PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(provinceService.getDetail(id));
    }
    @GetMapping("/{id}/districts")
    public ResponseEntity<BaseResponse<List<DistrictDto>>> getWards(
            @PathVariable("id") Integer id,
            @RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(value = "size", defaultValue = "0") Integer size){
        Pageable paging = Paginator.GetPageable(currentPage, size);
        return ResponseEntity.ok().body(provinceService.getDistricts(id, paging));
    }
}
