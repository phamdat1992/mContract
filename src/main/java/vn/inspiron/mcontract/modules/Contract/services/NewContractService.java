package vn.inspiron.mcontract.modules.Contract.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Contract.dto.NewContractDTO;
import vn.inspiron.mcontract.modules.Contract.model.ContractStatus;
import vn.inspiron.mcontract.modules.Entity.ContractEntity;
import vn.inspiron.mcontract.modules.Entity.FileEntity;
import vn.inspiron.mcontract.modules.Entity.UserEntity;
import vn.inspiron.mcontract.modules.FileManagement.service.FileManageService;
import vn.inspiron.mcontract.modules.Repository.ContractRepository;
import vn.inspiron.mcontract.modules.Repository.FilesRepository;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class NewContractService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private FilesRepository filesRepository;
    @Autowired
    private FileManageService fileManageService;

    protected FileEntity uploadFile(NewContractDTO newContractDTO) throws Exception {
        byte[] fileData = Base64.getDecoder().decode(newContractDTO.getFileData());
        String newFileName = UUID.randomUUID().toString();
        if (!fileManageService.isPDF(fileData)) {
            throw new Exception("invalid PDF format");
        }

        fileManageService.uploadFile(newFileName, fileData);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(newContractDTO.getFileName());
        fileEntity.setKeyName(newFileName);
        fileEntity.setToken(UUID.randomUUID().toString());
        return filesRepository.save(fileEntity);
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
        return contractRepository.save(contract);
    }

    public void createContract(
            NewContractDTO newContractDTO,
            UserEntity userEntity
    ) throws Exception {
        FileEntity fileEntity = this.uploadFile(newContractDTO);
        ContractEntity contract = this.addNewContract(newContractDTO, userEntity, fileEntity);

        List<String> msts = new ArrayList<String>();
        List<String> emails = new ArrayList<String>();

/*
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
            //contractUser.setDescription(user.getDescription());
            contractUser.setName(user.getName());
            contractUser.setFkContract(idContract);
            contractUser.setFkContractStatus(2L);
            contractUser.setFkContractUserRole(2L);
            contractUser.setFkEmail(email.getId());
            contractUser.setFkMst(mst.getId());
            contractUserRepository.save(contractUser);
        });*/
    }
}
