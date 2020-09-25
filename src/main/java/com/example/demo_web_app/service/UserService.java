package com.example.demo_web_app.service;

import com.example.demo_web_app.model.Role;
import com.example.demo_web_app.model.User;
import com.example.demo_web_app.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user==null){
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public boolean addUser(User user){

        User userFromDatabase = userRepository.findByUsername(user.getUsername());

        if(userFromDatabase!=null){
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        sendActivationCode(user);

        return true;
    }

    private void sendActivationCode(User user) {
        String message = String.format(
                "Hello, %s!\n" +
                "Welcome to Switter. Please, visit the link below:\n\n http://localhost:8080/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );

        if (!StringUtils.isEmpty(user.getEmail())){
            mailSender.sendMessage(user.getEmail(),"Activation code",message);
        }
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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(Long id, String username, String password, Map<String, String> form) {

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
    }

    public User findById(Long id){
        User user = userRepository.findById(id).orElseThrow();
        return user;
    }

    public void updateProfile(User user, String password, String email) {

        String userEmail = user.getEmail();

        boolean isEmailChanched = (email!=null && !email.equals(userEmail)) ||
                (userEmail!=null && !userEmail.equals(email));

        if(isEmailChanched){
            user.setEmail(email);
            if (!StringUtils.isEmpty(email)){
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        if(!StringUtils.isEmpty(password)){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);

        if(isEmailChanched) {
            sendActivationCode(user);
        }
    }
}
