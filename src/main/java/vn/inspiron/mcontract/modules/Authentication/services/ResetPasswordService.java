package vn.inspiron.mcontract.modules.Authentication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import vn.inspiron.mcontract.modules.Authentication.component.OnRegistrationCompleteEvent;
import vn.inspiron.mcontract.modules.Authentication.component.OnResetPasswordEvent;
import vn.inspiron.mcontract.modules.Authentication.dto.ResetPasswordDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.VerifyResetPasswordDTO;
import vn.inspiron.mcontract.modules.Entity.EmailEntity;
import vn.inspiron.mcontract.modules.Entity.UserEntity;
import vn.inspiron.mcontract.modules.Exceptions.UserExisted;
import vn.inspiron.mcontract.modules.Repository.EmailRepository;
import vn.inspiron.mcontract.modules.Repository.UserRepository;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Service
public class ResetPasswordService {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EncryptorAesGcmService encryptorAesGcmService;

    public void resetPass(ResetPasswordDTO resetPasswordDTO) throws Exception {
        Optional<EmailEntity> emailEntity = this.emailRepository.findByEmail(resetPasswordDTO.getEmail());
        if (emailEntity.isPresent()) {
            long retryDate = System.currentTimeMillis();
            Timestamp expireDate = Timestamp.from(new Timestamp(retryDate).toInstant().plusSeconds(600));
            String token = resetPasswordDTO.getEmail() + "-" + expireDate.getTime();
            this.sendResetPasswordEmail(
                    resetPasswordDTO.getEmail(),
                    this.encryptorAesGcmService.encrypt(token.getBytes())
            );
        }
    }

    public void sendResetPasswordEmail(String email, String token) {
        // Send verification email
        try {
            this.eventPublisher.publishEvent(new OnResetPasswordEvent(email, token));
        } catch (RuntimeException e) {
            // TODO: Handle email not sent exception
            e.printStackTrace();
        }
    }

    public void verifyResetPassword(VerifyResetPasswordDTO verifyResetPasswordDTO) throws Exception {
        String token = verifyResetPasswordDTO.getToken() + verifyResetPasswordDTO.getCode();
        token = this.encryptorAesGcmService.decrypt(token);
        String[] split = token.split("-");

        if (split.length > 1) {
            Timestamp expire = Timestamp.from(new Timestamp(Long.parseLong(split[split.length - 1])).toInstant());
            Timestamp current = Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant());

            if (current.after(expire)) {
                throw new Exception("The token is expired");
            }


        }
    }
}
