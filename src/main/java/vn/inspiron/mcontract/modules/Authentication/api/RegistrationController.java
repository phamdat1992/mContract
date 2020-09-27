package vn.inspiron.mcontract.modules.Authentication.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationResponseDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.VerifyTokenDTO;
import vn.inspiron.mcontract.modules.Authentication.services.RegistrationService;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;
import vn.inspiron.mcontract.modules.Exceptions.NotFound;

@RestController
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody UserRegistrationDTO userRegistrationDTO) throws Exception {

        try {
            registrationService.register(userRegistrationDTO);
        } catch (RuntimeException e) {
            throw new BadRequest();
        }
    }

    @PostMapping("/register/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verify(@RequestBody VerifyTokenDTO verifyTokenDTO) throws Exception {
        registrationService.verifyToken(verifyTokenDTO.getToken());
    }
}
