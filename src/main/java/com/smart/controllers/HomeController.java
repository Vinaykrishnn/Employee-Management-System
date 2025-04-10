package com.smart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.Users;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - smart contact manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - smart contact manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register - smart contact manager");
        model.addAttribute("user", new Users());
        return "signup";
    }

    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") Users user, BindingResult rBindingResult,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
            Model model, HttpSession session) {

        System.out.println("Agreement: " + agreement);
        System.out.println("User: " + user);

        try {
            if (!agreement) {
                System.out.println("Please agree to terms and conditions");
                throw new Exception("Please agree to terms and conditions");
            }

            // Check for validation errors
            if (rBindingResult.hasErrors()) {
                // Return to signup form with errors
                model.addAttribute("user", user);
                return "signup";
            }

            if (user.getEmail() == null || user.getEmail().isEmpty() ) {
                throw new Exception("Email is required.");
            }
            if(!user.getEmail().endsWith(".com")){
                throw new Exception("Email is required and must end with .com.");
            }
            if (user.getName() == null || user.getName().isEmpty()) {
                throw new Exception("Please enter your name.");
            }

            // Proceed with user registration
            user.setRole("ROLE_USER");
            user.setEnable(true);
            user.setImageUrl("default.jpeg");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            Users result = this.userRepository.save(user);
            // Clear the user object for the next registration attempt
//            model.addAttribute("user", new Users());
            model.addAttribute("user", result);

            session.setAttribute("message", new Message("Successfully registered", "alert-success"));
            return "redirect:/signup?success=true";

        } catch (Exception e) {
            e.printStackTrace();
            // If an error occurs, repopulate the user object and set an error message
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
            return "signup";
        }
    }

    @GetMapping("/login")
    public String customLogin(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";
    }
}
