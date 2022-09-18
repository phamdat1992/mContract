package vn.amitgroup.digitalsignatureapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import vn.amitgroup.digitalsignatureapi.entity.ContractTag;

public interface ContractTagRepository extends JpaRepository<ContractTag,Integer> {
    ContractTag findByContract_IdAndTag_Id(String contractId,Integer tagId);
}
