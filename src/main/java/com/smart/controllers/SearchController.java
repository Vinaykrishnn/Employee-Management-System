package com.smart.controllers;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contacts;
import com.smart.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;
    //search handler
    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){

        Users users = this.userRepository.getUserByUserName(principal.getName());
        List<Contacts> contacts = this.contactRepository.findByNameContainingAndUser(query, users);
        return ResponseEntity.ok(contacts);
    }
}
