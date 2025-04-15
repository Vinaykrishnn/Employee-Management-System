package com.smart.controllers;

import com.smart.dao.UserRepository;
import com.smart.entities.Users;
import com.smart.helper.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Random;

@Controller
public class ForgotController {

    Random random = new Random(1000);

    @RequestMapping("/forgot")
    public String openEmailForm(){
        return "forgotEmailForm";
    }

//    @PostMapping("/send-otp")
//    public String sentOTP(@RequestParam("email") String email){
//
//        //generating OTP of 4 digits
//
//        int otp = random.nextInt(9999);
//        System.out.println("OTP " + otp);
//        return "verifyOTP";
//    }
    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email") String email, HttpSession session, Model model) {
        int otp = random.nextInt(999999);
        session.setAttribute("myotp", otp);
        session.setAttribute("email", email);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Smart Contact Manager - OTP Verification");

            String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #e0e0e0; border-radius: 10px; padding: 20px; box-shadow: 0px 5px 10px rgba(0,0,0,0.1);\">" +
                    "<h2 style=\"color: #4A90E2; text-align: center;\">Smart Contact Manager</h2>" +
                    "<hr style=\"border-top: 1px solid #ddd;\"/>" +
                    "<p style=\"font-size: 16px; color: #333;\">Hi there 👋,</p>" +
                    "<p style=\"font-size: 16px; color: #333;\">We received a request to reset your password. Please use the OTP below to proceed:</p>" +
                    "<h1 style=\"text-align: center; color: #4A90E2; font-size: 40px; letter-spacing: 5px;\">" + otp + "</h1>" +
                    "<p style=\"font-size: 14px; color: #777;\">This OTP is valid for 10 minutes. If you didn’t request this, please ignore this email.</p>" +
                    "<br><p style=\"font-size: 14px; color: #777;\">Thanks,<br>Team Smart Contact Manager</p>" +
                    "</div>";

            helper.setText(htmlContent, true); // true for HTML

            mailSender.send(message);
            model.addAttribute("message", "We have successfully sent OTP to your registered Email");

            return "VerifyOtp";

        } catch (MessagingException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to send email. Please try again.");
            return "forgotEmailForm";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam("otp") int enteredOtp, HttpSession session, Model model) {
        int expectedOtp = (int) session.getAttribute("myotp");
        String email = (String) session.getAttribute("email");
        System.out.println("EMAIL from form: " + email);

        if (expectedOtp == enteredOtp) {
            // OTP matched
            return "changePassword";
        } else {
            model.addAttribute("error", "Invalid OTP! Please try again.");
            return "VerifyOtp";
        }
//        return "changePassword";
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session,
                                 Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "changePassword";
        }

        String email = (String) session.getAttribute("email");
        Users currentUser = this.userRepository.findByEmail(email);

        if (currentUser == null) {
            model.addAttribute("error", "User not found for email: " + email);
            return "changePassword";
        }

        currentUser.setPassword(this.bCryptPasswordEncoder.encode(confirmPassword));
        this.userRepository.save(currentUser);

        session.setAttribute("message", new Message("Your Password has been changed successfully!", "success"));
        return "redirect:/login";
    }


}
