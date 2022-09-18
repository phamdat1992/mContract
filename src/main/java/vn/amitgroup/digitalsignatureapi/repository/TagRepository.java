package vn.amitgroup.digitalsignatureapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.amitgroup.digitalsignatureapi.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query(value = "select t from Tag t join t.user u where u.email = :email and t.status ='0'")
    List<Tag> getAllByUserEmail(@Param("email") String email);
    @Query(value = "SELECT t.* FROM contract c,tag t,contract_tag ct,users u WHERE c.contract_id=ct.contract_id and ct.tag_id= t.tag_id "
    +"and t.status ='0' and t.user_id=u.user_id and u.email =:email and c.contract_id=:id order by ct.contract_tag_id ASC",nativeQuery = true)
    List<Tag> getAllByContractIdAndEmail(@Param("id")String id,@Param("email")String email);
    @Query(value = "SELECT t.tag_id,t.name,COUNT(c.contract_id),t.color_code FROM contract c,tag t,contract_tag ct "
    +"WHERE c.contract_id=ct.contract_id and ct.tag_id= t.tag_id and t.status ='0' and t.user_id=:id GROUP BY t.tag_id,t.name,t.color_code",nativeQuery = true)
    List<Object[]> getTagCount(@Param("id")Integer id);
}
