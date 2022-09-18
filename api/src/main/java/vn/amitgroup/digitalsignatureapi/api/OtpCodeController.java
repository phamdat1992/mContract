package vn.amitgroup.digitalsignatureapi.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import vn.amitgroup.digitalsignatureapi.consummer.ChatModule;
import vn.amitgroup.digitalsignatureapi.dto.*;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.User;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.*;


@RestController
@RequestMapping("/api/otps")
public class OtpCodeController {
    private final String SIGNIN_TYPE = "SIGNIN13@98qt";
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private OtpCodeService codeService;
    @Autowired
    private UserService userService;
    @Value("${my.email}")
    private String sendFrom;
    @Value("${register.email.subject}")
    private String subject;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    ChatModule chatModule;

    @Operation(summary = "send otp code for email register")
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<String>> sendCodeforRegiter(@Valid @RequestBody UserMail email)
            throws MessagingException, IOException,ApiException, ErrorCodeException {
        User user= userService.findByEmail(email.getEmail());
        if(user!=null){
            throw ErrorCodeException.AccountException();
        }
        Mail mail = new Mail();
        mail.setFrom(sendFrom);
        mail.setMailTo(email.getEmail());
        mail.setSubject("[HỆ THỐNG] Mã xác thực tài khoản MContract  ");
        mail.setTemplateName("register");
        Map<String, Object> model = new HashMap<String, Object>();
        Integer code = codeService.generateOTP(SIGNIN_TYPE + email.getEmail());
        model.put("code", String.valueOf(code));
        model.put("username","");
        mail.setProps(model);
        emailSenderService.sendEmailAsync(mail,true);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }

    @Operation(summary = "send otp code for email after login")
    @PostMapping(value = "/login")
    public ResponseEntity<BaseResponse<String>> sendCodeforSignIn(@Valid @RequestBody UserMail email)
            throws MessagingException, IOException,ApiException{
        User user= userService.findByEmail(email.getEmail());
        if(user==null){
            throw ErrorCodeException.AccountNotFoundException();
        }
        Mail mail = new Mail();
        mail.setFrom(sendFrom);
        mail.setMailTo(email.getEmail());
        mail.setSubject("[HỆ THỐNG] Mã xác thực tài khoản MContract");
        mail.setTemplateName("register");
        Map<String, Object> model = new HashMap<String, Object>();
        Integer code = codeService.generateOTP(SIGNIN_TYPE + email.getEmail());
        model.put("code", String.valueOf(code));
        if(StringUtils.isBlank(user.getFullName()) || user.getFullName()==null)
        {
            model.put("username", "");
        }
        else {
            model.put("username", " "+ String.valueOf(user.getFullName()));
        }

        mail.setProps(model);
        emailSenderService.sendEmailAsync(mail,true);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
}
