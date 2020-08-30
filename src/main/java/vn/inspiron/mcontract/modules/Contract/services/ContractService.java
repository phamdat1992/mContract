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
import vn.inspiron.mcontract.modules.User.dto.UserResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractMessageRepository contractMessageRepository;
    
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
                EmailEntity emailEntity = emailRepository.findByFkUser(userEntity.getId()).orElseThrow(() -> new BadRequest("Don't exist user with mail"));
                Long emailId = emailEntity.getId();
                contractPage = contractRepository.getAllContract(emailId, pageable);
                break;
            case SEND: // 3
                contractPage = contractRepository.getContractByCondition(null, userEntity.getId(), pageable, null);
                break;
            case DRAFT: // 5
                contractStatusId = contractStatusRepository.getByName(ContractStatusEnum.DRAFT.getValue()).getId();
                contractPage = contractRepository.getContractByCondition(contractStatusId, userEntity.getId(), pageable, null);
                break;
            case BOOKMARK_STAR: // 7
                contractPage = contractRepository.getContractByCondition(null, userEntity.getId(), pageable, bookmarkStar);
                break;
            case CANCEL: // 9
                contractStatusId = contractStatusRepository.getByName(ContractStatusEnum.CANCELLED.getValue()).getId();
                contractPage = contractRepository.getContractByCondition(contractStatusId, userEntity.getId(), pageable, null);
                break;
            case WAIT_APPROVE: // 11
                contractStatusId = contractStatusRepository.getByName(ContractStatusEnum.WAITING_FOR_APPROVAL.getValue()).getId();
                contractPage = contractRepository.getAllContractByContractStatus(userEntity.getId(), Arrays.asList(contractStatusId), pageable);
                break;
            case EXPIRY: // 13
                contractPage =  contractRepository.getContractExpire30(0.3F, userEntity.getId(), pageable);
                break;
            case SIGNED: // 15
                contractStatusId = contractStatusRepository.getByName(ContractStatusEnum.SIGNED.getValue()).getId();
                contractPage = contractRepository.getContractByCondition(contractStatusId, userEntity.getId(), pageable, null);
                break;
            case INVALID_CERTIFICATE: //17
                List<ContractStatusEntity> contractStatusEntities = contractStatusRepository.findAll();
                contractPage = contractRepository.getAllContractByContractStatus(userEntity.getId(), getListContractStatusIdByInvalidCer(contractStatusEntities), pageable);
                break;
            case NEED_SIGN: // 19
                contractStatusId = contractStatusRepository.getByName(ContractStatusEnum.WAITING_FOR_SIGNATURE.getValue()).getId();
                contractPage = contractRepository.getContractByCondition(contractStatusId, userEntity.getId(), pageable, null);
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
            contractResponse.setShortDescription(contractEntity.getDescription().length() < 100
                ? contractEntity.getDescription().substring(0, contractEntity.getDescription().length() - 1)
                : contractEntity.getDescription().substring(0, 100));
            contractResponse.setFileName(contractEntity.getFileName());
            
            CompanyEntity companyEntity = companyRepository.getFirstByFkMst(contractEntity.getFkMst());
            
            contractResponse.setCompanyName(companyEntity.getName());
            contractResponse.setCompanyAddress(companyEntity.getAddress());
            
            if (Objects.nonNull(contractEntity.getFkContractMessage())) {
                ContractMessageEntity contractMessageEntity = contractMessageRepository.findById(contractEntity.getFkContractMessage()).get();
                contractResponse.setLatestContentMessage(contractMessageEntity.getMessage());
            }
    
            List<UserResponse> userResponses = contractUserRepository.getAllByFkContract(contractEntity.getId()).stream()
                .map(contractUserEntity -> {
                    UserResponse userResponse = new UserResponse();
                    EmailEntity emailEntity = emailRepository.findById(contractUserEntity.getFkEmail()).get();
                    
                    if (Objects.nonNull(emailEntity.getFkUser())) {
                        UserEntity userEntity1 = userRepository.findById(emailEntity.getFkUser()).get();
                        
                        userResponse.setFullname(userEntity1.getFullname());
                        userResponse.setPhone(userEntity1.getPhone());
                        userResponse.setUsername(userEntity1.getUsername());
                    }
                    userResponse.setMail(emailEntity.getEmail());
                    return userResponse;
                }).collect(Collectors.toList());
            contractResponse.setUserResponse(userResponses);
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
