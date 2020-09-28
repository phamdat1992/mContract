package vn.inspiron.mcontract.modules.Authentication.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import vn.inspiron.mcontract.modules.Authentication.dto.*;
import vn.inspiron.mcontract.modules.Authentication.services.RegistrationService;
import vn.inspiron.mcontract.modules.Exceptions.BadRequest;
import vn.inspiron.mcontract.modules.Exceptions.NotFound;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.TimeZone;

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

    @PostMapping(value = "/check-account")
    public ResponseEntity<AccountResponseDTO> createJwtAuthenticationToken(@RequestBody AccountRequestDTO account, HttpServletRequest request, HttpServletResponse response) {
        try {
            ArrayList<String> message = this.registrationService.checkAccount(account);
            if (message.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(null);
            } else {
                AccountResponseDTO responseMessage = new AccountResponseDTO(message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/register/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verify(@RequestBody VerifyTokenDTO verifyTokenDTO) throws Exception {
        registrationService.verifyToken(verifyTokenDTO.getToken());
    }
}
