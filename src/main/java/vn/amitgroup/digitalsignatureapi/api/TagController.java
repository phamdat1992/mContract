package vn.amitgroup.digitalsignatureapi.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.amitgroup.digitalsignatureapi.dto.TagCount;
import vn.amitgroup.digitalsignatureapi.dto.TagDto;
import vn.amitgroup.digitalsignatureapi.dto.TagForm;
import vn.amitgroup.digitalsignatureapi.dto.UpdateTagForm;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.service.TagService;
import vn.amitgroup.digitalsignatureapi.utils.UserUtil;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping()
    public ResponseEntity<BaseResponse<List<TagDto>>> getAll() {
        return ResponseEntity.ok().body(new BaseResponse<>(tagService.getAll(UserUtil.email()), HttpStatus.OK.value()));
    }

    @GetMapping("/counts")
    public ResponseEntity<BaseResponse<List<TagCount>>> getListTagCount() {
        return ResponseEntity.ok().body(new BaseResponse<>(tagService.getListTagCount(UserUtil.email()), HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<TagDto>> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(new BaseResponse<>(tagService.getById(id), HttpStatus.OK.value()));
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<BaseResponse<List<TagDto>>> getByContractId(@PathVariable("contractId") String contractId) {
        return ResponseEntity.ok()
                .body(new BaseResponse<>(tagService.getByContractId(contractId,UserUtil.email()), HttpStatus.OK.value()));
    }

    @PostMapping()
    public ResponseEntity<BaseResponse<TagDto>> add(@RequestBody @Valid TagForm form) {
        return ResponseEntity.ok().body(new BaseResponse<>(tagService.add(form,UserUtil.email()), HttpStatus.OK.value()));
    }
    @PutMapping()
    public ResponseEntity<BaseResponse<TagDto>> update(@RequestBody @Valid UpdateTagForm form) {
        return ResponseEntity.ok().body(new BaseResponse<>(tagService.update(form), HttpStatus.OK.value()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> deleteById(@PathVariable("id") Integer id) {
        tagService.delete(id);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }

   

}
