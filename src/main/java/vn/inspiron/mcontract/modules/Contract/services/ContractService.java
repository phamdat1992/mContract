package vn.inspiron.mcontract.modules.Contract.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Common.data.type.ContractSearchType;
import vn.inspiron.mcontract.modules.Common.data.type.ContractStatusEnum;
import vn.inspiron.mcontract.modules.Common.util.MContractResponseBody;
import vn.inspiron.mcontract.modules.Contract.dto.ContractResponse;
import vn.inspiron.mcontract.modules.Contract.dto.NewContractDTO;
import vn.inspiron.mcontract.modules.Entity.*;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;
import vn.inspiron.mcontract.modules.Repository.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private MstRepository mstRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private ContractUserRepository contractUserRepository;
    @Autowired
    private ContractStatusRepository contractStatusRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public void getAllContract() {

    }

    public void createContract(NewContractDTO newContractDTO) {
        List<String> msts = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        ContractEntity contract = new ContractEntity();
        BeanUtils.copyProperties(newContractDTO, contract);
        contract.setFkContractStatus(2L);
        contract.setFkUser(1L);
        contract = contractRepository.save(contract);
        Long idContract = contract.getId();

        newContractDTO.getUserList().forEach((user) -> {
            msts.add(user.getMst());
            emails.add(user.getEmail());
        });

        List<MstEntity> listMst = mstRepository.findByMstIn(msts);
        List<EmailEntity> listEmail = emailRepository.findByEmailIn(emails);

        listMst.forEach((e) -> System.out.println(e.getMst()));
        listEmail.forEach((e) -> System.out.println(e.getEmail()));

        newContractDTO.getUserList().forEach((user) -> {
            List<MstEntity> findMst = listMst.stream().filter(
                    (mst) -> mst.getMst().equals(user.getMst())
            ).collect(Collectors.toList());

            List<EmailEntity> findEmail = listEmail.stream().filter(
                    (mst) -> mst.getEmail().equals(user.getEmail())
            ).collect(Collectors.toList());

            MstEntity mst = new MstEntity();
            if (findMst.isEmpty()) {
                mst.setMst(user.getMst());
                mst = mstRepository.save(mst);
                listMst.add(mst);
            } else {
                mst.setId(findMst.stream().findFirst().get().getId());
            }

            EmailEntity email = new EmailEntity();
            if (findEmail.isEmpty()) {
                email.setEmail(user.getEmail());
                email = emailRepository.save(email);
                listEmail.add(email);
            } else {
                email.setId(listEmail.stream().findFirst().get().getId());
            }

            ContractUserEntity contractUser = new ContractUserEntity();
            contractUser.setDescription(user.getDescription());
            contractUser.setName(user.getName());
            contractUser.setFkContract(idContract);
            contractUser.setFkContractStatus(2L);
            contractUser.setFkContractUserRole(2L);
            contractUser.setFkEmail(email.getId());
            contractUser.setFkMst(mst.getId());
            contractUserRepository.save(contractUser);
        });
    }
    
    public MContractResponseBody<List<ContractResponse>> getContractByCondition(UserEntity userEntity, ContractSearchType contractSearchType, int pageNumber, int pageSize, boolean bookmarkStar) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ContractEntity> contractPage;
        Long contractStatusId;
        switch (contractSearchType) {
            case ALL_CONTRACT: // 1
                Long emailId = emailRepository.findByFkUser(userEntity.getId()).get().getId();
                contractPage = contractRepository.getAllContract(emailId, pageable);
                break;
            case SEND: // 3
                contractPage = contractRepository.getContractEntitiesByFkUser(userEntity.getId(), pageable);
                break;
            case DRAFT: // 5
                contractStatusId = contractStatusRepository.getByName(ContractStatusEnum.DRAFT.getValue()).getId();
                contractPage = contractRepository.getContractEntitiesByFkContractStatus(contractStatusId, pageable);
                break;
            case BOOKMARK_STAR: // 7
                contractPage = contractRepository.getContractEntitiesByBookmarkStar(bookmarkStar, pageable);
                break;
            case CANCEL: // 9
                contractStatusId = contractStatusRepository.getByName(ContractStatusEnum.CANCELLED.getValue()).getId();
                contractPage = contractRepository.getContractEntitiesByFkContractStatus(contractStatusId, pageable);
                break;
            case WAIT_APPROVE: // 11
                contractStatusId = contractStatusRepository.getByName(ContractStatusEnum.WAITING_FOR_APPROVAL.getValue()).getId();
                contractPage = contractRepository.getAllContractByContractStatus(userEntity.getId(), Arrays.asList(contractStatusId), pageable);
                break;
            case EXPIRY: // 13
                contractPage =  contractRepository.getContractExpire30(0.3F, pageable);
                break;
            case SIGNED: // 15
                contractStatusId = contractStatusRepository.getByName(ContractStatusEnum.SIGNED.getValue()).getId();
                contractPage = contractRepository.getContractEntitiesByFkContractStatus(contractStatusId, pageable);
                break;
            case INVALID_CERTIFICATE: //17
                List<ContractStatusEntity> contractStatusEntities = contractStatusRepository.findAll();
                contractPage = contractRepository.getAllContractByContractStatus(userEntity.getId(), getListContractStatusIdByInvalidCer(contractStatusEntities), pageable);
                break;
            case NEED_SIGN: // 19
                contractStatusId = contractStatusRepository.getByName(ContractStatusEnum.WAITING_FOR_SIGNATURE.getValue()).getId();
                contractPage = contractRepository.getContractEntitiesByFkContractStatus(contractStatusId, pageable);
                break;
            default:
                throw new BadRequest("Unsupport condition type search");
        }
    
        // convert data
        List<ContractResponse> contractResponses = contractPage.getContent().stream().map(contractEntity -> {
            ContractResponse contractResponse = new ContractResponse();
            contractResponse.setId(contractEntity.getId().toString()); // hash
            contractResponse.setTitle(contractEntity.getTitle());
            contractResponse.setDescription(contractEntity.getDescription());
            contractResponse.setFileName(contractEntity.getFileName());
            
            CompanyEntity companyEntity = companyRepository.getByFkMst(contractEntity.getFkMst());
            
            contractResponse.setCompanyName(companyEntity.getName());
            contractResponse.setCompanyAddress(companyEntity.getAddress());
            return contractResponse;
        }).collect(Collectors.toList());
        
        MContractResponseBody<List<ContractResponse>> mContractResponseBody = new MContractResponseBody();
        mContractResponseBody.setData(contractResponses);
        mContractResponseBody.setTotalCount(contractPage.getTotalElements());
    
        return mContractResponseBody;
    }
    
    private List<Long> getListContractStatusIdByInvalidCer(List<ContractStatusEntity> contractStatusEntities) {
        return contractStatusEntities.stream().filter(contractStatusEntity -> ContractStatusEnum.INVALID_CERT.getValue().equalsIgnoreCase(contractStatusEntity.getName())
                                                                                || ContractStatusEnum.INVALID_ALGORITHM.getValue().equalsIgnoreCase(contractStatusEntity.getName())
                                                                                || ContractStatusEnum.INVALID_SIGNATURE.getValue().equalsIgnoreCase(contractStatusEntity.getName())
                                                                                || ContractStatusEnum.EXPIRED_CERTIFICATE.getValue().equalsIgnoreCase(contractStatusEntity.getName())
                                                                                || ContractStatusEnum.REVOKED_CERTIFICATE.getValue().equalsIgnoreCase(contractStatusEntity.getName())
                                                                                || ContractStatusEnum.MISMATCH_TAX_CODE.getValue().equalsIgnoreCase(contractStatusEntity.getName())
                                            ).map(ContractStatusEntity::getId)
                                            .collect(Collectors.toList());
    }
}
