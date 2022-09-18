package vn.amitgroup.digitalsignatureapi.service;

import java.util.List;

import vn.amitgroup.digitalsignatureapi.dto.TagCount;
import vn.amitgroup.digitalsignatureapi.dto.TagDto;
import vn.amitgroup.digitalsignatureapi.dto.TagForm;
import vn.amitgroup.digitalsignatureapi.dto.UpdateTagForm;

public interface TagService {
    TagDto add(TagForm tagForm,String email);
    TagDto update(UpdateTagForm tagForm);
    List<TagDto> getAll(String email);
    List<TagDto> getByContractId(String id,String email);
    TagDto getById(Integer id);
    void delete(Integer id);
    List<TagCount> getListTagCount(String email);
}
