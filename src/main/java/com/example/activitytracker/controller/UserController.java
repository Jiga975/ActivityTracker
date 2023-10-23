package com.example.activitytracker.controller;

import com.example.activitytracker.dto.requestDto.UserDto;
import com.example.activitytracker.model.User;
import com.example.activitytracker.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String defaultPage(){
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String IndexPage(){
        return "/index";
    }

    @GetMapping("/register")
    public String registerUser(Model model){
        model.addAttribute("users",new User());

        return "register";
    }
    @PostMapping("/user-register")
    public String register(@Validated @ModelAttribute UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Validation failed, return to the registration page with errors
            return "register";
        } else {
            userService.RegisterUser(userDto);
            return "redirect:/login_page";
        }
    }

    @GetMapping("/login_page")
    public String loginUser(Model model){
        model.addAttribute("users",new User());
        return "login_page";
    }
    @PostMapping("/user-login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession httpSession){

       if(email.isEmpty() || password.isEmpty()){
           return "login_page";
       }
       Boolean isValidUser = userService.loginUser(email,password);
       User user = userService.getUserByEmail(email).orElse(null);

       if (isValidUser){
           assert user != null;
           httpSession.setAttribute("userName", user.getFirstName());
           httpSession.setAttribute("userId", user.getId());
           return "redirect:/user_page";
       }else {
           return "login_page";
       }
    }
}
