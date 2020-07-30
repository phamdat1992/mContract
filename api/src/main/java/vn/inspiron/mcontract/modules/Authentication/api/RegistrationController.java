package vn.inspiron.mcontract.modules.Authentication.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationDTO;
import vn.inspiron.mcontract.modules.Authentication.dto.UserRegistrationResponseDTO;
import vn.inspiron.mcontract.modules.Authentication.services.RegistrationService;

@RestController
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserRegistrationResponseDTO> register(@RequestBody UserRegistrationDTO userRegistrationDTO) throws Exception {

        UserRegistrationResponseDTO response = new UserRegistrationResponseDTO();
        String token = registrationService.register(userRegistrationDTO);
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/register/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verify(@RequestParam("token") String token) throws Exception {
        registrationService.verifyToken(token);
    }


}
