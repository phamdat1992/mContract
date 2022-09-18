package vn.amitgroup.digitalsignatureapi.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import vn.amitgroup.digitalsignatureapi.dto.Mail;
import vn.amitgroup.digitalsignatureapi.dto.QuestionDto;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.service.EmailSenderService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final String SUBJECT_MAIL="have a question from ";
    @Autowired
    private EmailSenderService emailSenderService;
    @Value("${question.email}")
    private String questionEmail;
    @Operation(summary = "send question to system email")
    @PostMapping()
    public ResponseEntity<BaseResponse<String>> send(@Valid @RequestBody QuestionDto dto) throws MessagingException, IOException { 
        Mail mail = new Mail();
        mail.setFrom(dto.getEmail());
        mail.setMailTo(questionEmail);
        mail.setSubject(SUBJECT_MAIL+dto.getEmail());
        mail.setTemplateName("question");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("fullName", dto.getFullName());
        model.put("email", dto.getEmail());
        model.put("phoneNumber", dto.getPhoneNumber());
        model.put("content", dto.getContent());
        model.put("companyName", dto.getCompanyName());
        mail.setProps(model);
        emailSenderService.sendEmail(mail);
        return ResponseEntity.ok().body(new BaseResponse<>() );
    }
    
}
