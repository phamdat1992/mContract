package vn.amitgroup.digitalsignatureapi.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.amitgroup.digitalsignatureapi.dto.SignerDto;
import vn.amitgroup.digitalsignatureapi.dto.SignerInfo;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.Signer;
import vn.amitgroup.digitalsignatureapi.entity.SignerContract;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.SignerContractRepository;
import vn.amitgroup.digitalsignatureapi.repository.SignerRepository;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.SignerService;

@Service
public class SignerServiceImpl implements SignerService {
    @Autowired
    SignerRepository signerRepository;
    @Autowired
    private SignerContractRepository signerContractRepository;
    @Autowired
    JwtProvider jwtProvider;

    @Override
    public Signer getSigner(SignerInfo signerInfo) {
        return signerRepository.findByEmailAndContractId(signerInfo.getSignerEmail(), signerInfo.getContracId());
    }

    public Signer getSignerByToken(String token) {
        SignerInfo signerInfo = jwtProvider.getSignerInfo(token);
        return getSigner(signerInfo);
    }

    @Override
    public String getEmaiByTokenSigner(String token) {
        String email = jwtProvider.getSignerInfo(token).getSignerEmail();
        return email;
    }

    @Override
    public SignerDto getSigner(String token) throws ApiException {
        SignerInfo signerInfo = jwtProvider.getSignerInfo(token);
        SignerContract signerContract = signerContractRepository
                .findByContract_IdAndSigner_Email(signerInfo.getContracId(), signerInfo.getSignerEmail());
        if (signerContract == null) {
            throw ErrorCodeException.NullException();
        }
        SignerDto signerDto = new ModelMapper().map(signerContract, SignerDto.class);
        signerDto.setCompanyName(signerContract.getSigner().getCompanyName());
        signerDto.setEmail(signerContract.getSigner().getEmail());
        signerDto.setFullName(signerContract.getSigner().getFullName());
        signerDto.setTaxCode(signerContract.getSigner().getTaxCode());
        signerDto.setId(signerContract.getSigner().getId());
        if (signerContract.getSigner().getUser() != null) {
            signerDto.setAvatarPath(signerContract.getSigner().getUser().getAvatarPath());
        }

        return signerDto;
    }

}
