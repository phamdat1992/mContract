package vn.inspiron.mcontract.modules.Contract.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Contract.dto.NewContractDTO;
import vn.inspiron.mcontract.modules.Contract.model.ContractStatus;
import vn.inspiron.mcontract.modules.Contract.model.ContractUserRole;
import vn.inspiron.mcontract.modules.Entity.*;
import vn.inspiron.mcontract.modules.FileManagement.service.FileManageService;
import vn.inspiron.mcontract.modules.Repository.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NewContractService {
    @Autowired
    private ContractUserRepository contractUserRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private MstRepository mstRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private FilesRepository filesRepository;
    @Autowired
    private FileManageService fileManageService;

    protected FileEntity uploadFile(NewContractDTO newContractDTO, UserEntity userEntity) throws Exception {
        byte[] fileData = Base64.getDecoder().decode(newContractDTO.getFileData());
        String newFileName = UUID.randomUUID().toString();
        if (!this.fileManageService.isPDF(fileData)) {
            throw new Exception("invalid PDF format");
        }

        this.fileManageService.uploadFile(newFileName, fileData, userEntity);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(newContractDTO.getFileName());
        fileEntity.setKeyName(newFileName);
        fileEntity.setToken(UUID.randomUUID().toString());
        return this.filesRepository.save(fileEntity);
    }

    protected ContractEntity addNewContract(
            NewContractDTO newContractDTO,
            UserEntity userEntity,
            FileEntity fileEntity
    ) {
        ContractEntity contract = new ContractEntity();
        contract.setDescription(newContractDTO.getDescription());
        contract.setTitle(newContractDTO.getTitle());
        contract.setExpiryDateSigned(newContractDTO.getExpiryDateSigned());
        contract.setFkContractStatus((long) ContractStatus.WAITING_FOR_SIGNATURE.getValue());
        contract.setFkFile(fileEntity.getId());
        contract.setFkUser(userEntity.getId());
        return this.contractRepository.save(contract);
    }

    protected void addInvolvedGuysContract(
            NewContractDTO newContractDTO,
            ContractEntity contract
    ) {
        List<String> msts = new ArrayList<>();
        List<String> emails = new ArrayList<>();

        newContractDTO.getUserList().forEach((user) -> {
            msts.add(user.getMst());
            emails.add(user.getEmail());
        });

        List<MstEntity> listMst = this.mstRepository.findByMstIn(msts);
        List<EmailEntity> listEmail = this.emailRepository.findByEmailIn(emails);

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
                mst = this.mstRepository.save(mst);
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
                email.setId(findEmail.stream().findFirst().get().getId());
            }

            ContractUserEntity contractUser = new ContractUserEntity();
            contractUser.setName(user.getName());
            contractUser.setFkContract(contract.getId());
            contractUser.setFkContractStatus((long) ContractStatus.WAITING_FOR_SIGNATURE.getValue());
            contractUser.setFkContractUserRole((long) ContractUserRole.SIGNER.getValue());
            contractUser.setFkEmail(email.getId());
            contractUser.setFkMst(mst.getId());
            this.contractUserRepository.save(contractUser);
        });
    }

    public void createContract(
            NewContractDTO newContractDTO,
            UserEntity userEntity
    ) throws Exception {
        FileEntity fileEntity = this.uploadFile(newContractDTO, userEntity);
        ContractEntity contract = this.addNewContract(newContractDTO, userEntity, fileEntity);
        this.addInvolvedGuysContract(newContractDTO, contract);
    }
}
