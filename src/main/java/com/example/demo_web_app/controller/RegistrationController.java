package com.example.demo_web_app.controller;

import com.example.demo_web_app.model.User;
import com.example.demo_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

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
    public String addUser(@RequestParam("password2")String passwordConfirm,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model){

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);

        if (isConfirmEmpty){
            model.addAttribute("password2Error","Password confirmation cannot be empty");
        }

        if (user.getPassword()!=null && !user.getPassword().equals(passwordConfirm)){
            model.addAttribute("passwordError","Passwords are different");
        }

        if(isConfirmEmpty || bindingResult.hasErrors()){

            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.addUser(user)){
            model.addAttribute("existError","User already exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model){

        boolean isActivated = userService.activateUser(code);

        System.out.println(isActivated);

        if (isActivated){
            model.addAttribute("successType","success");
            model.addAttribute("message","Email successfully activated");
        }else {
            model.addAttribute("successType","danger");
            model.addAttribute("message","Activation code is not found");
        }
        return "login";
    }
}
