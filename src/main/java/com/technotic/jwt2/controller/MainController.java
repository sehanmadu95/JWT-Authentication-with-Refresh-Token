package com.technotic.jwt2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/main")
public class MainController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminOnly(){
        return "Authenticated :: ADMIN ONLY";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String userOnly(){
        return "Authenticated :: USER ONLY";
    }


    @GetMapping("/all")
    public String aall(){
        return "Authenticated :: ADMIN & USER";
    }


    @GetMapping("/welcome")
    public String Welcome(){
        return "WELCOME NO NEED AUTHENTICATION....";
    }
}
