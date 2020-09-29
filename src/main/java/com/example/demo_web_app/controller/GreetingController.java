package com.example.demo_web_app.controller;

import com.example.demo_web_app.model.Message;
import com.example.demo_web_app.model.User;
import com.example.demo_web_app.repos.MessageRepository;
import com.example.demo_web_app.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class GreetingController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Value(value = "${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false,defaultValue = "") String tag,
                       Model model){

        Iterable<Message> messages;

        if (tag != null && !tag.isEmpty()){
            messages = messageRepository.findByTag(tag);
        }else {
            messages = messageRepository.findAll();
        }

        model.addAttribute("messages",messages);
        model.addAttribute("filter",tag);
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam("file") MultipartFile file,
                      @ModelAttribute @Valid Message message,
                      BindingResult bindingResult,
                      Model model) throws IOException {

            message.setAuthor(user);
            // validation of message fields
            if (bindingResult.hasErrors() || file.isEmpty()){
                System.out.println("some errors happened...");
                Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
                // put the map of errors into model to show them if needed
                model.mergeAttributes(errorsMap);
                // put entered message
                model.addAttribute("message",message);
                // validation of file input
                if(file.isEmpty()){
                    model.addAttribute("fileNotFound","Please choose the file");
                }
            }else {
                if (file != null && !file.getOriginalFilename().isEmpty()) {
                    File uploadDir = new File(uploadPath);

                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }
                    String uuidFile = UUID.randomUUID().toString();
                    String resultFilename = uuidFile + "." + file.getOriginalFilename();
                    file.transferTo(new File(uploadPath + "/" + resultFilename));
                    message.setFilename(resultFilename);
                }
                model.addAttribute("message",null);
                messageRepository.save(message);
            }

            // put all messages into model to show them on main page
        Iterable<Message> messages = messageRepository.findAll();
        model.addAttribute("messages",messages);

        return "main";
    }

    @GetMapping("/messages/{id}")
    public String userMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable Long id,
                               Model model){

        if (userRepository.findById(id).isPresent()){
        User user = userRepository.findById(id).get();
        Set<Message> messages = user.getMessages();

        model.addAttribute("messages",messages);
        model.addAttribute("isCurrentUser",user.equals(currentUser));
        model.addAttribute("user",user);
        }else {
            return "redirect:/main";
        }
        return "userMessages";
    }
}
