package vn.inspiron.mcontract.modules.Authentication.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.inspiron.mcontract.modules.Authentication.dto.ResetPasswordDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.VerifyResetPasswordDTO;
import vn.inspiron.mcontract.modules.Authentication.services.RegistrationService;
import vn.inspiron.mcontract.modules.Authentication.services.ResetPasswordService;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;

@RestController
public class ResetPasswordController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody ResetPasswordDTO resetPasswordDTO) throws Exception {
        try {
            this.resetPasswordService.resetPass(resetPasswordDTO);
        } catch (RuntimeException e) {
            throw new BadRequest();
        }
    }

    @PostMapping("/verify-reset-password")
    @ResponseStatus(HttpStatus.OK)
    public void verifyResetPassword(@RequestBody VerifyResetPasswordDTO verifyResetPasswordDTO) throws Exception {
        try {
            this.resetPasswordService.verifyResetPassword(verifyResetPasswordDTO);
        } catch (RuntimeException e) {
            throw new BadRequest();
        }
    }
}
