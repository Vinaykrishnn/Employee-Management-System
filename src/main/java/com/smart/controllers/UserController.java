package com.smart.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.smart.dao.ContactRepository;
import com.smart.entities.Contacts;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.smart.dao.UserRepository;
import com.smart.entities.Users;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String username = principal.getName();
        System.out.println("Username " + username);

        Users user = userRepository.getUserByUserName(username);
        System.out.println("User " + user);
        model.addAttribute("user", user);
    }

    //Dashboard home
    @RequestMapping("/index")
    public String userDashboard(Model model, Principal principal) {

        model.addAttribute("title", "User Dashboard");
        return "normal/userDashboard";
    }

    //Open add form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model, Principal principal, HttpSession session) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contacts());
        session.removeAttribute("message");
        return "normal/addContact";
    }

    //processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contacts contacts, @RequestParam("profileImage") MultipartFile file,
                                 Principal principal, HttpSession session) {
        try {
            String name = principal.getName();
            Users user = this.userRepository.getUserByUserName(name);
            contacts.setUser(user);

            if (file.isEmpty()) {
                contacts.setImage("contact.png");
            } else {
                contacts.setImage(file.getOriginalFilename());
                File imageFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(imageFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image uploaded");
            }


            user.getContacts().add(contacts);
            this.userRepository.save(user);
            System.out.println("Contact" + contacts);
            System.out.println("Contact added");
            session.setAttribute("message", new Message("Your contact is added", "success"));
//            session.removeAttribute("message");
        } catch (Exception exception) {
            System.out.println("Error " + exception);
            exception.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong!! Try again", "danger"));

        }
        return "normal/addContact";
    }

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal){
        model.addAttribute("title", "Show Contacts");

        String userName=principal.getName();
        Users user = this.userRepository.getUserByUserName(userName);

        Pageable pageable = PageRequest.of(page,5);
        Page<Contacts> contacts = this.contactRepository.findContactByUserId(user.getId(), pageable);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());

        return "normal/showContacts";
    }

    @RequestMapping("/contact/{contactId}")
    public String showContactDetails(@PathVariable("contactId") String contactId, Model model, Principal principal){

        Optional<Contacts> contactsOptional = this.contactRepository.findById(Integer.valueOf(contactId));
        Contacts contact  = contactsOptional.get();

        String username = principal.getName();
        Users user = this.userRepository.getUserByUserName(username);

        if(user.getId() == contact.getUser().getId()){
            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());
        }else{
            model.addAttribute("alert", "No contact exists with that contact ID.");
        }

        return "normal/contactDetails";
    }

    @PostMapping("/clear-session-message")
    public ResponseEntity<Void> clearSessionMessage(HttpSession session) {
        session.removeAttribute("message");
        return ResponseEntity.ok().build();
    }


    @GetMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") Integer contactId, Model model, Principal principal, HttpSession session) {
        Optional<Contacts> contactsOptional = this.contactRepository.findById(contactId);

        if (!contactsOptional.isPresent()) {
            session.setAttribute("message", new Message("Contact not found!", "danger"));
            return "redirect:/user/show-contacts/0";
        }

        Contacts contact = contactsOptional.get();
        String username = principal.getName();
        Users user = this.userRepository.getUserByUserName(username);

        if (user.getId().equals(contact.getUser().getId())) {
            this.contactRepository.delete(contact);
            session.setAttribute("message", new Message("Contact has been deleted successfully", "success"));
        } else {
            session.setAttribute("message", new Message("You don't have permission to delete this contact!", "danger"));
        }

        return "redirect:/user/show-contacts/0";
    }

    //open update form
    @PostMapping("/update-contact/{contactId}")
    public String updateForm(@PathVariable("contactId") Integer contactId ,Model model,@ModelAttribute Contacts contact
    ){
        model.addAttribute("title","Update Contact");
        Contacts contacts = this.contactRepository.findById(contactId).get();
        model.addAttribute("contact",contacts);
        return "normal/updateForm";
    }

    //update contact handler
    @PostMapping(value="/process-update")
    public String updateHandler(@ModelAttribute Contacts contacts,  @RequestParam("profileImage") MultipartFile file,
                                Model model, HttpSession httpSession, Principal principal){

        try{
            Contacts oldContact = this.contactRepository.findById(contacts.getContactId()).get();
            if(!file.isEmpty()){
                File imageFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(imageFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image uploaded");
                contacts.setImage(file.getOriginalFilename());
            }else{
                contacts.setImage(oldContact.getImage());
            }
            Users users = this.userRepository.getUserByUserName(principal.getName());
            contacts.setUser(users);
            this.contactRepository.save(contacts);
            httpSession.setAttribute("message",new Message("Your contact has been updated","success"));
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return "normal/showContacts";

    }

    //profile handler
    @GetMapping("/profile")
    public String userProfile(Model model,Principal principal){
        model.addAttribute("title", "User Profile");
//        String username = principal.getName();


        Users user = this.userRepository.getUserByUserName(principal.getName());
        if (user.getContacts() == null) {
            user.setContacts(new ArrayList<>()); // Avoid null
        }
        model.addAttribute("user", user);
        return "normal/userProfile";
    }
}