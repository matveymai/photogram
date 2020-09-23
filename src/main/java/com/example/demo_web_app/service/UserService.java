package com.example.demo_web_app.service;

import com.example.demo_web_app.model.Role;
import com.example.demo_web_app.model.User;
import com.example.demo_web_app.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user){

        User userFromDatabase = userRepository.findByUsername(user.getUsername());

        if(userFromDatabase!=null){
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);

        String message = String.format(
                "Hello, %s!\n" +
                "Welcome to Switter. Please, visit the link below:\n\n http://localhost:8080/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );

        if (!StringUtils.isEmpty(user.getEmail())){
            mailSender.sendMessage(user.getEmail(),"Activation code",message);
        }

        return true;
    }

    public boolean activateUser(String code){

        User user = userRepository.findByActivationCode(code);

        if (user == null){
            return false;
        }

        user.setActivationCode(null);

        userRepository.save(user);

        return true;
    }
}
