package com.pingfrommorocco.appid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("login")
    public String viewPublic() {
        return "login.html";
    }
}