package com.example.demo_web_app.controller;

import com.example.demo_web_app.model.User;
import com.example.demo_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){

        if (!userService.addUser(user)){
            model.addAttribute("message","User already exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model){

        boolean isActivated = userService.activateUser(code);

        System.out.println(isActivated);

        if (isActivated){
            model.addAttribute("message","Email successfully activated");
        }else {
            model.addAttribute("message","Activation code is not found");
        }
        return "login";
    }
}
