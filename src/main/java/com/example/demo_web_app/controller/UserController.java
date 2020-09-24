package com.example.demo_web_app.controller;

import com.example.demo_web_app.model.Role;
import com.example.demo_web_app.model.User;
import com.example.demo_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){
        model.addAttribute("users",userService.findAll());
        return "userList";
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public String userEdit(@PathVariable Long id, Model model){

        model.addAttribute("user",userService.findById(id));
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping
    public String userEditAndSet(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam Long id,
                                 @RequestParam Map<String,String> form){

        userService.saveUser(id,username,password,form);

        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model){

        model.addAttribute("username",user.getUsername());
        model.addAttribute("password",user.getPassword());
        model.addAttribute("email",user.getEmail());

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String password,
                                @RequestParam String email){

       userService.updateProfile(user,password,email);

        return "redirect:/user/profile";
    }


}
