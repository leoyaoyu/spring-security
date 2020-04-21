package com.ibm.esw.leo.spring.security.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class MainController {

    @RequestMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute( "loginError"  , true);
        return "login";
    }

    @GetMapping("/401")
    public String accessDenied() {
        return "401";
    }

    @GetMapping("/user/common")
    public String common() {
        return "user/common";
    }

    @GetMapping("/user/admin")
    public String admin() {
        return "user/admin";
    }

    @GetMapping("/user/authentication")
    public String getUserAuthentication() {
        String username = null;
        //当前认证通过的用户身份
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //用户身份
        Object principle = authentication.getPrincipal();
        if(principle == null) {
            username = "inlogged user";
        }

        if(principle instanceof UserDetails) {
            log.info("principle : {}", principle);
            username = ((UserDetails) principle).getUsername();
        }
        return "/user/"+username;
    }


}
