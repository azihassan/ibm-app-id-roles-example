package com.pingfrommorocco.appid;

import com.pingfrommorocco.appid.configuration.SecurityConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ApiController {
    Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    @GetMapping("/public")
    public String viewPublic() {
        return "This endpoint is public";
    }

    @GetMapping("/private")
    public String viewPrivate() {
        return "This endpoint is private";
    }

    @GetMapping("/admin")
    public String viewAdmin(Principal principal) {
        logger.debug("Principal : {}", principal);
        return "This endpoint is for admins only";
    }
}
