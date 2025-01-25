package com.tgerstel.infrastructure.login;

import com.tgerstel.domain.User;
import com.tgerstel.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private final UserRepository userRepo;

    public MyUserDetailsService (final UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userRepo.findUserByUsername(username);
        if (user != null) return user;        
        throw new UsernameNotFoundException("User: "+ username + " not found!");
    }
}
