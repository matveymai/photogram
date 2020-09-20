package com.example.demo_web_app.controller;

import com.example.demo_web_app.model.Role;
import com.example.demo_web_app.model.User;
import com.example.demo_web_app.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize(value = "hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users",userRepository.findAll());
        return "userList";
    }

    @GetMapping("/{id}")
    public String userEdit(@PathVariable Integer id, Model model){

        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user",user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userEditAndSet(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam Integer id,
                                 @RequestParam Map<String,String> form){

        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(username);
        user.setPassword(password);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()){
            if (roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);

        return "redirect:/user";
    }




}
