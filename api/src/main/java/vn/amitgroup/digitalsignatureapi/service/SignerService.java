package vn.amitgroup.digitalsignatureapi.service;

import vn.amitgroup.digitalsignatureapi.dto.SignerDto;

import vn.amitgroup.digitalsignatureapi.dto.SignerInfo;
import vn.amitgroup.digitalsignatureapi.entity.Signer;

public interface SignerService {
    Signer getSigner(SignerInfo signerInfo);
    Signer getSignerByToken(String token);
    SignerDto getSigner(String token);
    String getEmaiByTokenSigner(String token);
}
