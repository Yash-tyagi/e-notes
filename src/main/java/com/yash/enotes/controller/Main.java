package com.yash.enotes.controller;

import com.yash.enotes.entity.User;
import com.yash.enotes.service.IUserService.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Main {


    private final UserService userService;

    public Main(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @PostMapping(path="/createUser")
    public String createUser(@ModelAttribute User user, HttpSession session) {
        String error=null;
        try {
            System.out.println("user coming to server - " + user);
            User newUser = userService.createUser(user);
            System.out.println(newUser);
            if(newUser != null) error="success";
        }catch (Exception e) {
            error = e.getMessage();
            System.out.println(e.getMessage());
        }
        session.setAttribute("error", error);
        return "redirect:register";
    }

}
