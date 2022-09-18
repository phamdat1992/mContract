package com.ldonline.yousinghd.gcpupdateserver.controller;

import com.ldonline.yousinghd.gcpupdateserver.model.entities.UserEntity;
import com.ldonline.yousinghd.gcpupdateserver.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
public class TestRestController {

    private static final Logger LOG = Logger.getLogger(TestRestController.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Value("${show.message}")
    private String showMessage;

    @RequestMapping("/")
    public String home() {
        LOG.info(showMessage);
        return "Hello World!" + showMessage;
    }

    @GetMapping(path = "/add") // Map ONLY GET Requests
    public @ResponseBody
    String addNewUser(@RequestParam String username, @RequestParam String email) {
        UserEntity n = new UserEntity(username, email);
        userRepository.save(n);
        return "Saved";
    }


    @GetMapping(path = "/getByUsername") // Map ONLY GET Requests
    public @ResponseBody
    UserEntity addNewUser(@RequestParam String username) {
        UserEntity n = userRepository.getByUsername(username);
        return n;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping("/_ah/health")
    public String healthy() {
        return "Still survivingffddf .";
    }

}
