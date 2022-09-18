package vn.amitgroup.digitalsignatureapi.service.impl;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import vn.amitgroup.digitalsignatureapi.dto.CompanyAddForm;
import vn.amitgroup.digitalsignatureapi.dto.CompanyDto;
import vn.amitgroup.digitalsignatureapi.dto.CompanyUpdateForm;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.Company;
import vn.amitgroup.digitalsignatureapi.entity.Signer;
import vn.amitgroup.digitalsignatureapi.entity.User;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.CompanyRepository;
import vn.amitgroup.digitalsignatureapi.repository.SignerRepository;
import vn.amitgroup.digitalsignatureapi.repository.UserRepository;
import vn.amitgroup.digitalsignatureapi.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SignerRepository signerRepository;
    @Override
    @Transactional
    public CompanyDto update(CompanyUpdateForm company, String email) throws ApiException {
        
        Company entity = companyRepository.findByUser_Email(email);
        if(entity==null) {
            throw ErrorCodeException.NullException();
        }
        User user=userRepository.findByEmail(email);
        if(user==null) {
            throw ErrorCodeException.NullException();
          }
        entity.setName(company.getName());
        entity.setEmail(company.getEmail());
        entity.setFoundDate(company.getFoundDate());
        entity.setPhoneNumber(company.getPhoneNumber());
        entity.setTaxCode(company.getTaxCode());
        entity.setDistrictCode(company.getDistrictCode());
        entity.setWardCode(company.getWardCode());
        entity.setProviceCode(company.getProviceCode());
        entity.setAddress(company.getAddress());
        entity.setLogoPath(company.getLogoPath());
        List<Signer> signers=signerRepository.findByEmailAndTaxCode(email, company.getTaxCode());
        if(!CollectionUtils.isEmpty(signers)){
            signers.stream().forEach(s->{
                s.setUser(user);
            });
            signerRepository.saveAll(signers);
        }
        return  new ModelMapper().map(companyRepository.save(entity), CompanyDto.class);
    
    }

    @Override
    @Transactional
    public CompanyDto add(CompanyAddForm company, String email) throws ApiException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw ErrorCodeException.NullException();
        }
        ModelMapper mapper = new ModelMapper();
        Company entity = mapper.map(company, Company.class);
        entity.setUser(user);
        entity.setCreatedDate(new Date());
        List<Signer> signers=signerRepository.findByEmailAndTaxCode(email, company.getTaxCode());
        if(!CollectionUtils.isEmpty(signers)){
            signers.stream().forEach(s->{
                s.setUser(user);
            });
            signerRepository.saveAll(signers);
        }
        return mapper.map(companyRepository.save(entity), CompanyDto.class);

    }

    @Override
    public CompanyDto getByUser(String email) {
        if(companyRepository.findByUser_Email(email)!=null)
        {
            return new ModelMapper().map(companyRepository.findByUser_Email(email), CompanyDto.class);
        }
        return null;

    }

}
