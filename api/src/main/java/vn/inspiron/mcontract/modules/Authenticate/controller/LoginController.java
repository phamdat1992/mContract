package vn.inspiron.mcontract.modules.Authenticate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {
    @GetMapping("login")
    public String login() {
        System.out.println("test");
        return "login";
    }
}