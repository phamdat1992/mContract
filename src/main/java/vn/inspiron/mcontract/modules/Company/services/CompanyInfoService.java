package vn.inspiron.mcontract.modules.Company.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Company.dto.CompanyRegistrationDTO;
import vn.inspiron.mcontract.modules.Entity.CompanyEntity;
import vn.inspiron.mcontract.modules.Entity.MstEntity;
import vn.inspiron.mcontract.modules.Exceptions.MstExisted;
import vn.inspiron.mcontract.modules.Repository.CompanyRepository;
import vn.inspiron.mcontract.modules.Repository.MstRepository;

@Service
public class CompanyInfoService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    MstRepository mstRepository;

    public CompanyEntity register(CompanyRegistrationDTO companyRegistrationDTO) {

        if (!mstRepository.findByMst(companyRegistrationDTO.getMst()).isEmpty()) {
            throw new MstExisted();
        }

        CompanyEntity companyEntity = new CompanyEntity();
        BeanUtils.copyProperties(companyRegistrationDTO, companyEntity);
        companyRepository.save(companyEntity);
        MstEntity mstEntity = newMst(companyRegistrationDTO.getMst(), companyEntity);
        return companyEntity;
    }

    private MstEntity newMst(String mst, CompanyEntity companyEntity) {
        MstEntity mstEntity = new MstEntity();
        mstEntity.setMst(mst);
        mstEntity.setFkCompany(companyEntity.getId());
        mstRepository.save(mstEntity);
        return mstEntity;
    }
}
