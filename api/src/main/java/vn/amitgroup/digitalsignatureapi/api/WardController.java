package vn.amitgroup.digitalsignatureapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.amitgroup.digitalsignatureapi.dto.WardDto;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.utils.Paginator;
import vn.amitgroup.digitalsignatureapi.service.WardService;

import java.util.List;

@RestController
@RequestMapping("/api/common/wards")
public class WardController {
    @Autowired
    private WardService wardService;

    @GetMapping()
    public ResponseEntity<BaseResponse<List<WardDto>>> getAll(
            @RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(value = "size", defaultValue = "0") Integer size) {
        Pageable paging = Paginator.GetPageable(currentPage, size);
        return ResponseEntity.ok().body(wardService.getAll(paging));
    }
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<WardDto>> getDetailProvince(
            @PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(wardService.getDetail(id));
    }
}
