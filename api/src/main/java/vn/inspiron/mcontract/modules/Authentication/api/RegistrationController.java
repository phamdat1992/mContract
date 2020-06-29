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
    public ResponseEntity<UserRegistrationResponseDTO> register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserRegistrationResponseDTO response = new UserRegistrationResponseDTO();
        try
        {
            registrationService.register(userRegistrationDTO);
            response.setUsername(userRegistrationDTO.getUsername());
            response.setEmail(userRegistrationDTO.getEmail());
            return ResponseEntity.ok(response);

        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }
}
