package vn.inspiron.mcontract.modules.Contract.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.inspiron.mcontract.modules.Common.data.type.ContractSearchType;
import vn.inspiron.mcontract.modules.Common.data.type.ContractStatusEnum;
import vn.inspiron.mcontract.modules.Common.util.MContractResponseBody;
import vn.inspiron.mcontract.modules.Contract.dto.ContractMessageResponse;
import vn.inspiron.mcontract.modules.Contract.dto.ContractResponse;
import vn.inspiron.mcontract.modules.Contract.dto.NewContractDTO;
import vn.inspiron.mcontract.modules.Contract.model.ContractStatus;
import vn.inspiron.mcontract.modules.Entity.*;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;
import vn.inspiron.mcontract.modules.FileManagement.service.FileManageService;
import vn.inspiron.mcontract.modules.Repository.*;
import vn.inspiron.mcontract.modules.User.dto.UserResponse;

import java.util.*;
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
    @Autowired
    private FilesRepository filesRepository;
    
    public void getAllContract() {

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
    
    public MContractResponseBody<ContractResponse> bookmarkContract(Long contractId, boolean bookmark, Long userId) {
        ContractEntity contractEntity = contractRepository.findByIdAndFkUser(contractId, userId).orElseThrow(() -> new BadRequest("Contract ID: " + contractId + " not exist with user"));
        contractEntity.setBookmarkStar(bookmark);
        contractEntity = contractRepository.save(contractEntity);
        MContractResponseBody responseBody = new MContractResponseBody();
        ContractResponse contractResponse = new ContractResponse();
        contractResponse.setId(contractEntity.getId().toString());
        contractResponse.setBookmarkStar(contractEntity.isBookmarkStar());
        responseBody.setData(contractResponse);
        
        return responseBody;
    }
    
    public MContractResponseBody<ContractResponse> getDetailContractForUser(Long contractId, Long userId, String mail) {
        ContractEntity contractEntity;
        if (Objects.nonNull(userId)) {
            contractEntity = contractRepository.findByIdAndFkUser(contractId, userId).orElseThrow(() -> new BadRequest("Contract ID: " + contractId + " not exist with user"));
        } else if (Objects.nonNull(mail)) {
            contractUserRepository.getByFkContractAndFkEmail(contractId, emailRepository.findByEmail(mail).get().getId()).orElseThrow(() -> new BadRequest("Contract ID: " + contractId + " not exist with guest"));
            contractEntity = contractRepository.findById(contractId).get();
        } else {
            throw new BadRequest("You can't access this contract");
        }
        
        List<ContractUserEntity> contractUsers = contractUserRepository.getAllByFkContract(contractId);
        List<UserResponse> userResponses = new ArrayList<UserResponse>();
        ContractResponse contractResponse = new ContractResponse();
        
        // set user own contract
        UserResponse userOwnContract = new UserResponse();
        UserEntity userEntity = userRepository.findById(contractEntity.getFkUser()).get();
        userOwnContract.setUsername(userEntity.getUsername());
        userOwnContract.setFullname(userEntity.getFullname());
        userOwnContract.setMail(emailRepository.findByFkUser(userEntity.getId()).get().getEmail());
        userOwnContract.setPhone(userEntity.getPhone());
        userResponses.add(userOwnContract);
        
        //set user relate with contract
        userResponses.addAll(contractUsers.stream().map(contractUserEntity -> {
            EmailEntity emailEntity = emailRepository.findById(contractUserEntity.getFkEmail()).get();
            UserResponse userResponse = new UserResponse();
            userResponse.setMail(emailEntity.getEmail());
            if (Objects.nonNull(emailEntity.getFkUser())) {
                userRepository.findById(emailEntity.getFkUser()).ifPresent(userEntity1 -> {
                    userResponse.setFullname(userEntity1.getFullname());
                    userResponse.setUsername(userEntity1.getUsername());
                    userResponse.setPhone(userEntity1.getPhone());
                });
            }
            return userResponse;
        }).collect(Collectors.toList()));
        
        List<ContractMessageResponse> contractMessageResponses = contractMessageRepository.getAllByFkContractOrderByIdDesc(contractEntity.getId()).stream().map(contractMessageEntity -> {
            ContractMessageResponse contractMessageResponse = new ContractMessageResponse();
            contractMessageResponse.setMail(emailRepository.findById(contractMessageEntity.getFkMail()).get().getEmail());
            contractMessageResponse.setMessage(contractMessageEntity.getMessage());
            return contractMessageResponse;
        }).collect(Collectors.toList());
    
    
        // set contract
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
        contractResponse.setUserResponse(userResponses);
        contractResponse.setContractMessageResponses(contractMessageResponses);
        contractResponse.setUrlFile(filesRepository.findById(contractEntity.getFkFile()).get().getUploadPath()); // generate url temp
        
        MContractResponseBody<ContractResponse> responseBody = new MContractResponseBody<>();
        responseBody.setData(contractResponse);
        return responseBody;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public MContractResponseBody<Boolean> updateContractMessage(ContractMessageResponse message, Long userId, Long contractId) {
        ContractMessageEntity entity = new ContractMessageEntity();
    
        EmailEntity emailEntity;
        if (Objects.nonNull(userId)) {
            emailEntity = emailRepository.findByFkUser(userId).get();
        } else {
            emailEntity = emailRepository.findByEmail(message.getMail()).get();
        }
        
        ContractEntity contractEntity = contractRepository.findById(contractId).get();
        Optional<ContractUserEntity> optContractUser = contractUserRepository.getByFkContractAndFkEmail(contractId, emailEntity.getId());
        
        if (Objects.nonNull(userId)) {
            if (!contractEntity.getFkUser().equals(userId) && !optContractUser.isPresent()) {
                throw new BadRequest("User is not own this contract");
            }
        } else {
            if (!contractUserRepository.getByFkContractAndFkEmail(contractId, emailEntity.getId()).isPresent()) {
                throw new BadRequest("Guest don't relate this contract");
            }
        }
        
        entity.setMessage(message.getMessage());
        entity.setFkMail(emailEntity.getId());
        entity.setFkContract(contractId);
    
        MContractResponseBody<Boolean> responseBody = new MContractResponseBody<>();
        try {
            ContractMessageEntity entity1 = contractMessageRepository.save(entity);
            contractEntity.setFkContractMessage(entity1.getId());
            contractRepository.save(contractEntity);
            responseBody.setData(true);
        } catch (Exception e) {
            responseBody.setData(false);
        }
        return responseBody;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public MContractResponseBody<Boolean> cancelContractByUser(Long userId, Long contractId) {
        EmailEntity emailEntity = emailRepository.findByFkUser(userId).get();
        Long cancelStatusId = contractStatusRepository.getByName(ContractStatusEnum.CANCELLED.getValue()).getId();
        Long signedStatusId = contractStatusRepository.getByName(ContractStatusEnum.SIGNED.getValue()).getId();
        
        Optional<ContractUserEntity> optContractEntity = contractUserRepository.getByFkContractAndFkEmail(contractId, emailEntity.getId());
        if (optContractEntity.isPresent()) {
            ContractUserEntity contractUserEntity = optContractEntity.get();
            if (contractUserEntity.getFkContractStatus().equals(signedStatusId)) {
                throw new BadRequest("Can't cancel when Contract was signed");
            }
            contractUserEntity.setFkContractStatus(cancelStatusId);
            contractUserRepository.save(contractUserEntity);
    
            ContractEntity contractEntity = contractRepository.findById(contractId).get();
            contractEntity.setFkContractStatus(cancelStatusId);
            contractRepository.save(contractEntity);
        } else {
            ContractEntity contractEntity = contractRepository.findById(contractId).get();
            contractEntity.setFkContractStatus(cancelStatusId);
            contractRepository.save(contractEntity);
        }
    
        MContractResponseBody<Boolean> responseBody = new MContractResponseBody<>();
        responseBody.setData(true);
        return responseBody;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public MContractResponseBody<Boolean> cancelContractByGuest(String mail, Long contractId) {
        EmailEntity emailEntity = emailRepository.findByEmail(mail).get();
        Long cancelStatusId = contractStatusRepository.getByName(ContractStatusEnum.CANCELLED.getValue()).getId();
        Long signedStatusId = contractStatusRepository.getByName(ContractStatusEnum.SIGNED.getValue()).getId();
        
        Optional<ContractUserEntity> optContractEntity = contractUserRepository.getByFkContractAndFkEmail(contractId, emailEntity.getId());
        if (optContractEntity.isPresent()) {
            ContractUserEntity contractUserEntity = optContractEntity.get();
            if (contractUserEntity.getFkContractStatus().equals(signedStatusId)) {
                throw new BadRequest("Can't cancel when Contract was signed");
            }
            contractUserEntity.setFkContractStatus(cancelStatusId);
            contractUserRepository.save(contractUserEntity);
        
            ContractEntity contractEntity = contractRepository.findById(contractId).get();
            contractEntity.setFkContractStatus(cancelStatusId);
            contractRepository.save(contractEntity);
        }
        
        ContractEntity contractEntity = contractRepository.findById(contractId).get();
        contractEntity.setFkContractStatus(cancelStatusId);
        contractRepository.save(contractEntity);
        
        MContractResponseBody<Boolean> responseBody = new MContractResponseBody<>();
        responseBody.setData(true);
        return responseBody;
    }
}
