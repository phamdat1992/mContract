package com.ldonline.yousinghd.gcpupdateserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestRythmController {
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "llllloooo");
        return "home";
    }
}
