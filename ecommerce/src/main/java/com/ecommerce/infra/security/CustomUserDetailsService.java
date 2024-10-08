package com.ecommerce.infra.security;

import com.ecommerce.entities.user.UserModel;
import com.ecommerce.exceptions.EventNotFoundException;
import com.ecommerce.repositories.user.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class CustomUserDetailsService implements UserDetailsService {
    
    private final IUserRepository repository;

    public CustomUserDetailsService(IUserRepository repository){
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserModel user = this.repository.findByEmail(username).orElseThrow(() -> new EventNotFoundException(""));
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}