package com.example.demo_web_app.model;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@SuppressWarnings("ALL")
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private boolean active;

    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;
}
