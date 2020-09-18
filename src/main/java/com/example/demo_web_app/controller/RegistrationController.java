package com.example.demo_web_app.controller;

import com.example.demo_web_app.model.Role;
import com.example.demo_web_app.model.User;
import com.example.demo_web_app.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){

        User userFromDatabase = userRepository.findByUsername(user.getUsername());

        if (userFromDatabase!=null){
            model.addAttribute("message","User already exists!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        return "redirect:/login";
    }






}
