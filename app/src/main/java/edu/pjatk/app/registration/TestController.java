package edu.pjatk.app.registration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/user")
    public String testUser(){
        return "Only user should see that";
    }

    @GetMapping("/admin")
    public String testAdmin(){
        return "Only admin should see that";
    }
}
