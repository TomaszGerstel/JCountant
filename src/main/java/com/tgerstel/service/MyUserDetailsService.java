package com.tgerstel.service;

import com.tgerstel.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.tgerstel.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private UserRepository userRepo;


    @Autowired
    public MyUserDetailsService (UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {    	
        User user = userRepo.findByUsername(username);
        if (user != null) return user;        
        throw new UsernameNotFoundException("User: "+ username + " not found!");
    }
}
