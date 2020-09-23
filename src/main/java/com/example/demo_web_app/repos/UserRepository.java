package com.example.demo_web_app.repos;

import com.example.demo_web_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);

    User findByActivationCode(String code);
}
