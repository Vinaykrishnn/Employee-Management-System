package com.smart.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.entities.Users;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userRepository.getUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not found user!!");
        }

        CutsomerUserDetails cutsomerUserDetails = new CutsomerUserDetails(user);
        return cutsomerUserDetails;
    }

}
