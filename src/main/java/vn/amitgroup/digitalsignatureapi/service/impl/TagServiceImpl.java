package vn.amitgroup.digitalsignatureapi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import vn.amitgroup.digitalsignatureapi.dto.TagCount;
import vn.amitgroup.digitalsignatureapi.dto.TagDto;
import vn.amitgroup.digitalsignatureapi.dto.TagForm;
import vn.amitgroup.digitalsignatureapi.dto.UpdateTagForm;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.Tag;
import vn.amitgroup.digitalsignatureapi.entity.User;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.TagRepository;
import vn.amitgroup.digitalsignatureapi.repository.UserRepository;
import vn.amitgroup.digitalsignatureapi.service.TagService;

@Service
@Slf4j
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public TagDto add(TagForm tagForm,String email) {
        log.info("save new tag");
        try {
            User user = userRepository.findByEmail(email);
            Tag entity = new Tag();
            entity.setName(tagForm.getName());
            entity.setColorCode(tagForm.getColorCode());
            entity.setStatus("0");
            entity.setUser(user);
            Tag tag= tagRepository.save(entity);
            TagDto dto= new TagDto();
            dto.setId(tag.getId());
            dto.setName(tag.getName());
            dto.setColorCode(tag.getColorCode());
            return dto;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<TagDto> getAll(String email) {
        log.info("get all list ");
        try {
            List<TagDto> list= new ArrayList<>();
            tagRepository.getAllByUserEmail(email).stream().forEach(tag->{
                list.add(new ModelMapper().map(tag, TagDto.class));
            });;
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public TagDto getById(Integer id) throws ApiException {
        
            Optional<Tag> tag= tagRepository.findById(id);
            if(!tag.isPresent()){
                throw ErrorCodeException.NullException();
            }
            return new ModelMapper().map(tag.get(), TagDto.class);
       
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("get a Tag by id");
        try {
            Tag tag = tagRepository.findById(id).get();
            tag.setStatus("1");
            tagRepository.save(tag);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<TagDto> getByContractId(String id,String email) {
        log.info("get all list by contract id");
        try {
            List<TagDto> list= new ArrayList<>();
             tagRepository.getAllByContractIdAndEmail(id,email).stream().forEach(tag->{
                TagDto dto= new TagDto();
                dto.setId(tag.getId());
                dto.setName(tag.getName());
                list.add(dto);
             });
             return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<TagCount> getListTagCount(String email) throws ApiException{
        log.info("get all list by contract id");
        User user= userRepository.findByEmail(email);
        if(user==null){
            throw ErrorCodeException.NullException();
		}
        try {
            List<Object[]> list= tagRepository.getTagCount(user.getId());
            List<Tag> tagList =tagRepository.getAllByUserEmail(email);
            List<TagCount> tagCounts= new ArrayList<>();
            Map<Integer,TagCount> map= new HashMap<>();
            tagList.stream().forEach(tag->{
                TagCount count= new TagCount();
                count.setId(tag.getId());
                count.setName(tag.getName());
                count.setColorCode(tag.getColorCode());
                count.setCountContract(0);
                map.put(tag.getId(), count);
            });
            if(list.size()>0){
                list.stream().forEach(o->{
                    TagCount count= new TagCount();
                    count.setId(Integer.parseInt(o[0].toString()));
                    count.setName(o[1].toString());
                    count.setCountContract(Integer.parseInt(o[2].toString()));
                    count.setColorCode(o[3].toString());
                    map.put(Integer.parseInt(o[0].toString()), count);
                });
            }
            map.entrySet().stream().forEach(m->{
                tagCounts.add(m.getValue());
            });
            return tagCounts;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public TagDto update(UpdateTagForm tagForm) {
       Tag tag=tagRepository.findById(tagForm.getId()).get();
       tag.setColorCode(tagForm.getColorCode());
       tag.setName(tagForm.getName());
       tagRepository.save(tag);
        return new ModelMapper().map(tag, TagDto.class);
    }

}
