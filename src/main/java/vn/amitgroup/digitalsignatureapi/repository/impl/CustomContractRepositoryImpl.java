package vn.amitgroup.digitalsignatureapi.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import vn.amitgroup.digitalsignatureapi.dto.ContractDto;
import vn.amitgroup.digitalsignatureapi.dto.SignerDto;
import vn.amitgroup.digitalsignatureapi.dto.TagDto;
import vn.amitgroup.digitalsignatureapi.dto.UserDto;
import vn.amitgroup.digitalsignatureapi.entity.SignerContract;
import vn.amitgroup.digitalsignatureapi.entity.Tag;
import vn.amitgroup.digitalsignatureapi.entity.User;
import vn.amitgroup.digitalsignatureapi.repository.CustomContractRepository;
import vn.amitgroup.digitalsignatureapi.repository.SignerContractRepository;
import vn.amitgroup.digitalsignatureapi.repository.TagRepository;
import vn.amitgroup.digitalsignatureapi.utils.ConvertUtil;
@Repository
public class CustomContractRepositoryImpl implements CustomContractRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private SignerContractRepository signerContractRepository;
    @Override
    public Page<ContractDto> search(Map<String,String> map,Pageable page,String email) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("email", email);
        if (StringUtils.isNotBlank(map.get("fromDate"))
                && StringUtils.isNotBlank(map.get("toDate"))) {
            parameters.addValue("dateStart", map.get("fromDate"));
            parameters.addValue("dateEnd", map.get("toDate"));
        }
        else{
            if(StringUtils.isNotBlank(map.get("fromDate"))){
                parameters.addValue("dateStart", map.get("fromDate"));
            }
            if(StringUtils.isNotBlank(map.get("toDate"))){
                parameters.addValue("dateEnd", map.get("toDate"));
            }
        }
        if (StringUtils.isNotBlank(map.get("tagId"))) {
            parameters.addValue("tagId",Integer.valueOf( map.get("tagId")));
        }
        if (StringUtils.isNotBlank(map.get("search"))) {
            parameters.addValue("search", ConvertUtil.convertStringToNormalized(map.get("search")));
        }
        if (StringUtils.isNotBlank(map.get("partner"))) {
            parameters.addValue("fullName",ConvertUtil.convertStringToNormalized(map.get("partner")));
        }
        if (StringUtils.isNotBlank(map.get("topic"))) {
            parameters.addValue("title",ConvertUtil.convertStringToNormalized( map.get("topic")));
        }
        if (StringUtils.isNotBlank(map.get("type"))) {
            parameters.addValue("role", map.get("type"));
        }
        if (StringUtils.isNotBlank(map.get("status"))) {
            parameters.addValue("status", map.get("status"));
        }
        StringBuffer queryString = new StringBuffer();
        queryString.append(
                "SELECT c.contract_id as id ,c.content as content,c.created_time as createdTime,c.expiration_time as expirationTime");
        queryString.append(",c.file_name as fileName,c.file_id as fileId,sc.contract_status as status,c.title as title,sc.contract_role as role,sc.is_watched as watched");
        queryString.append(",(select count(*) from comment cm where cm.contract_id=c.contract_id) as countComment");
        queryString.append(" FROM contract c,signer_contract sc,signer s,users u");
        if (StringUtils.isNotBlank(map.get("tagId"))) {
            queryString.append(", tag t, contract_tag ct"); //nếu có điều kiện tagId thì join thêm bảng
        }
        queryString.append(" WHERE c.contract_id=sc.contract_id AND u.user_id=s.user_id AND s.signer_id=sc.signer_id AND sc.is_delected is not true ");
        if (StringUtils.isNotBlank(map.get("tagId"))) {
            queryString.append(" AND t.user_id=u.user_id AND ct.tag_id=t.tag_id AND ct.contract_id=c.contract_id AND t.tag_id =:tagId");//set tag id để tìm kiếm
        }
        queryString.append(" AND u.email=:email");
        if (StringUtils.isNotBlank(map.get("fromDate"))
                && StringUtils.isNotBlank(map.get("toDate"))) {
            queryString.append(" AND DATE(c.created_time) BETWEEN TO_DATE(:dateStart,'YYYY-MM-DD') AND TO_DATE(:dateEnd,'YYYY-MM-DD')");
        }
        else{
            if(StringUtils.isNotBlank(map.get("fromDate"))){
                queryString.append(" AND DATE(c.created_time) >= TO_DATE(:dateStart,'YYYY-MM-DD') ");
            }
            if(StringUtils.isNotBlank(map.get("toDate"))){
                queryString.append(" AND DATE(c.created_time) <= TO_DATE(:dateEnd,'YYYY-MM-DD')");
            }
        }
        if (StringUtils.isNotBlank(map.get("search"))) {
            queryString.append(" AND ( lower(c.normal_content) like lower(concat('%', concat(:search, '%'))) or lower(c.normal_title) like lower(concat('%', concat(:search, '%'))))");
        }
        if (StringUtils.isNotBlank(map.get("partner"))) {
            queryString.append(" AND c.contract_id in (SELECT ca.contract_id FROM contract ca, signer_contract sca, signer sa WHERE ca.contract_id=sca.contract_id AND sca.signer_id = sa.signer_id AND lower(sa.normal_name) like lower(concat('%', concat(:fullName, '%'))))");
        }
        if (StringUtils.isNotBlank(map.get("topic"))) {
            queryString.append(" AND lower(c.normal_title) like lower(concat('%', concat(:title, '%')))");
        }
        if (StringUtils.isNotBlank(map.get("status"))) {
           switch (map.get("status")) {
               case "PROCESSING":
                    queryString.append(" AND sc.is_signed is not true AND sc.contract_status not in ('AUTHENTICATIONFAIL','COMPLETE','EXPIRED','CANCEL')");//nếu bản thân user chưa kí thì trạng thái là đang xử lí
                   break;
                case "WAITINGFORPARTNER":
                    queryString.append(" AND sc.contract_status= 'WAITINGFORPARTNER'"); 
                    break;
               default:
                    queryString.append(" AND sc.contract_status = :status");
                   break;
           }
        }
        if (StringUtils.isNotBlank((map.get("type")))) {
            queryString.append(" AND sc.contract_role =:role");
        }
        if (StringUtils.isNotBlank(map.get("status"))){
            if(map.get("status").equals("WAITINGFORPARTNER")){
                queryString.append(" GROUP BY id ,content,createdTime,expirationTime,fileName,fileId,status,title,sc.contract_role,watched,countComment HAVING (SELECT COUNT(*) FROM contract c1,signer_contract sc1,signer s1 WHERE c1.contract_id=sc1.contract_id AND s1.signer_id=sc1.signer_id AND s1.email !=:email and c1.contract_id=c.contract_id and sc1.is_signed is not true) >0");
            }
        }
        if(StringUtils.isNotBlank(map.get("sortByDate"))){
            queryString.append(" ORDER BY c.created_time "+map.get("sortByDate"));
        }else{
            queryString.append(" ORDER BY c.update_time DESC");
        }
        Integer count= jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ("+queryString.toString()+") AS a" ,parameters, Integer.class);
        if(page!=null){
            queryString.append(" LIMIT " + page.getPageSize() + " OFFSET " + page.getOffset());
        }
        List<ContractDto> result = jdbcTemplate.query(queryString.toString(), parameters, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                ContractDto contact = new ContractDto();
                contact.setId(rs.getString("id"));
                contact.setCreatedTime(new Date(rs.getTimestamp("createdTime").getTime()));
                contact.setExpirationTime(new Date(rs.getTimestamp("expirationTime").getTime()));
                contact.setContent(rs.getString("content"));
                contact.setTitle(rs.getString("title"));
                contact.setStatus(rs.getString("status"));
                contact.setWathched(rs.getBoolean("watched"));
                contact.setFileName(rs.getString("fileName"));
                contact.setTitle(rs.getString("title"));
                contact.setType(rs.getString("role"));
                contact.setFileId(rs.getLong("fileId"));
                contact.setCountCommnet(rs.getInt("countComment"));
                return contact;
            }
        });
        List<ContractDto> dtos= new ArrayList<>();
        for (ContractDto contractDto : result) {
            User userCreate = signerContractRepository.getUserCreateContract(contractDto.getId());
            contractDto.setUserCreate(new ModelMapper().map(userCreate, UserDto.class) );
            List<SignerContract> signerContracts= signerContractRepository.findByContract_IdOrderByCreatedTimeAsc(contractDto.getId());
            if(signerContracts.size()>0){
                List<SignerDto> signerDtos=signerContracts.stream().map(s->{
                    SignerDto signerDto= new ModelMapper().map(s, SignerDto.class);
                    signerDto.setCompanyName(s.getSigner().getCompanyName());
                    signerDto.setEmail(s.getSigner().getEmail());
                    signerDto.setFullName(s.getSigner().getFullName());
                    signerDto.setTaxCode(s.getSigner().getTaxCode());
                    if(s.getSigner().getUser()!=null){
                        signerDto.setAvatarPath(s.getSigner().getUser().getAvatarPath());
                    }
                    if(contractDto.getStatus().equals("WAITINGFORPARTNER")&& !(s.getIsSigned()) && s.getSigner().getEmail().equals(email)){ //kiểm tra xem user này đã kí chưa
                        contractDto.setStatus("PROCESSING");
                    }
                    return signerDto;
                }).collect(Collectors.toList());
                contractDto.setSignerDtos(signerDtos);
               
            } 
            List<Tag> tags= tagRepository.getAllByContractIdAndEmail(contractDto.getId(),email);
            if(tags.size()>0){
                List<TagDto> tagDtos=tags.stream().map(t->{
                    TagDto tagDto= new ModelMapper().map(t, TagDto.class);
                    return tagDto;
                }).collect(Collectors.toList());
                contractDto.setTagDtos(tagDtos);
            } 
            dtos.add(contractDto);
        }
        return new PageImpl<ContractDto>(dtos, page, count);
    }

}
