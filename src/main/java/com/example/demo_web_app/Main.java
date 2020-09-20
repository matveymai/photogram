package com.example.demo_web_app;

import com.example.demo_web_app.model.Role;
import com.example.demo_web_app.model.User;

import java.util.*;

public class Main {
    public static void main(String[] args) {

    User user = new User();
    System.out.println(Arrays.toString(Role.values()));
    EnumSet<Role> roles = EnumSet.allOf(Role.class);
    user.setRoles(roles);
    System.out.println(user.getRoles());

        for (Role role : roles) {
            System.out.println(role);
        }

    }
}