package vn.inspiron.mcontract.modules.Contract.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Common.data.type.ContractSearchType;
import vn.inspiron.mcontract.modules.Common.data.type.ContractStatusEnum;
import vn.inspiron.mcontract.modules.Common.util.MContractResponseBody;
import vn.inspiron.mcontract.modules.Contract.dto.ContractResponse;
import vn.inspiron.mcontract.modules.Entity.*;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;
import vn.inspiron.mcontract.modules.Repository.*;
import vn.inspiron.mcontract.modules.User.dto.UserResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PagingContractService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractUserRepository contractUserRepository;
    @Autowired
    private ContractMessageRepository contractMessageRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ContractStatusRepository contractStatusRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private EmailRepository emailRepository;

    public void getAllContract() {

    }

    public MContractResponseBody<List<ContractResponse>> getContractByCondition(UserEntity userEntity, ContractSearchType contractSearchType, int pageNumber, int pageSize, boolean bookmarkStar) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ContractEntity> contractPage;
        Long contractStatusId;
        switch (contractSearchType) {
            case ALL_CONTRACT: // 1
                EmailEntity emailEntity = this.emailRepository.findByFkUser(userEntity.getId()).orElseThrow(() -> new BadRequest("Don't exist user with mail"));
                Long emailId = emailEntity.getId();
                contractPage = this.contractRepository.getAllContract(emailId, pageable);
                break;
            case SEND: // 3
                contractPage = this.contractRepository.getContractByCondition(null, userEntity.getId(), pageable, null);
                break;
            case DRAFT: // 5
                contractStatusId = this.contractStatusRepository.getByName(ContractStatusEnum.DRAFT.getValue()).getId();
                contractPage = this.contractRepository.getContractByCondition(contractStatusId, userEntity.getId(), pageable, null);
                break;
            case BOOKMARK_STAR: // 7
                contractPage = this.contractRepository.getContractByCondition(null, userEntity.getId(), pageable, bookmarkStar);
                break;
            case CANCEL: // 9
                contractStatusId = this.contractStatusRepository.getByName(ContractStatusEnum.CANCELLED.getValue()).getId();
                contractPage = this.contractRepository.getContractByCondition(contractStatusId, userEntity.getId(), pageable, null);
                break;
            case WAIT_APPROVE: // 11
                contractStatusId = this.contractStatusRepository.getByName(ContractStatusEnum.WAITING_FOR_APPROVAL.getValue()).getId();
                contractPage = this.contractRepository.getAllContractByContractStatus(userEntity.getId(), Arrays.asList(contractStatusId), pageable);
                break;
            case EXPIRY: // 13
                contractPage =  this.contractRepository.getContractExpire30(0.3F, userEntity.getId(), pageable);
                break;
            case SIGNED: // 15
                contractStatusId = this.contractStatusRepository.getByName(ContractStatusEnum.SIGNED.getValue()).getId();
                contractPage = this.contractRepository.getContractByCondition(contractStatusId, userEntity.getId(), pageable, null);
                break;
            case INVALID_CERTIFICATE: //17
                List<ContractStatusEntity> contractStatusEntities = this.contractStatusRepository.findAll();
                contractPage = this.contractRepository.getAllContractByContractStatus(userEntity.getId(), getListContractStatusIdByInvalidCer(contractStatusEntities), pageable);
                break;
            case NEED_SIGN: // 19
                contractStatusId = this.contractStatusRepository.getByName(ContractStatusEnum.WAITING_FOR_SIGNATURE.getValue()).getId();
                contractPage = this.contractRepository.getContractByCondition(contractStatusId, userEntity.getId(), pageable, null);
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

            CompanyEntity companyEntity = this.companyRepository.getFirstByFkMst(contractEntity.getFkMst());

            contractResponse.setCompanyName(companyEntity.getName());
            contractResponse.setCompanyAddress(companyEntity.getAddress());

            if (Objects.nonNull(contractEntity.getFkContractMessage())) {
                ContractMessageEntity contractMessageEntity = this.contractMessageRepository.findById(contractEntity.getFkContractMessage()).get();
                contractResponse.setLatestContentMessage(contractMessageEntity.getMessage());
            }

            List<UserResponse> userResponses = this.contractUserRepository.getAllByFkContract(contractEntity.getId()).stream()
                    .map(contractUserEntity -> {
                        UserResponse userResponse = new UserResponse();
                        EmailEntity emailEntity = this.emailRepository.findById(contractUserEntity.getFkEmail()).get();

                        if (Objects.nonNull(emailEntity.getFkUser())) {
                            UserEntity userEntity1 = this.userRepository.findById(emailEntity.getFkUser()).get();

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
