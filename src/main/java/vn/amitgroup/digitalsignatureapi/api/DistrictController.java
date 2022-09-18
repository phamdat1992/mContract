package vn.amitgroup.digitalsignatureapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.amitgroup.digitalsignatureapi.dto.DistrictDto;
import vn.amitgroup.digitalsignatureapi.dto.WardDto;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.utils.Paginator;
import vn.amitgroup.digitalsignatureapi.service.DistrictService;

import java.util.List;

@RestController
@RequestMapping("/api/common/districts")
public class DistrictController {
    @Autowired
    private DistrictService districtService;

    @GetMapping()
    public ResponseEntity<BaseResponse<List<DistrictDto>>> getAll(
            @RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(value = "size", defaultValue = "0") Integer size) {
        Pageable paging = Paginator.GetPageable(currentPage, size);
        return ResponseEntity.ok().body(districtService.getAll(paging));
    }
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<DistrictDto>> getDetailProvince(
            @PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(districtService.getDetail(id));
    }
    @GetMapping("/{id}/wards")
    public ResponseEntity<BaseResponse<List<WardDto>>> getWards(
            @PathVariable("id") Integer id,
            @RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(value = "size", defaultValue = "0") Integer size){
        Pageable paging = Paginator.GetPageable(currentPage, size);
        return ResponseEntity.ok().body(districtService.getWards(id, paging));
    }
}
