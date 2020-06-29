package vn.inspiron.mcontract.modules.Contract.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Contract.dto.NewContractDTO;
import vn.inspiron.mcontract.modules.Entity.ContractEntity;
import vn.inspiron.mcontract.modules.Entity.ContractUserEntity;
import vn.inspiron.mcontract.modules.Entity.EmailEntity;
import vn.inspiron.mcontract.modules.Entity.MstEntity;
import vn.inspiron.mcontract.modules.Repository.ContractRepository;
import vn.inspiron.mcontract.modules.Repository.ContractUserRepository;
import vn.inspiron.mcontract.modules.Repository.EmailRepository;
import vn.inspiron.mcontract.modules.Repository.MstRepository;

import java.util.ArrayList;
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
}
