package com.ecommerce.infra.security;

import com.ecommerce.entities.user.UserModel;
import com.ecommerce.exceptions.EventNotFoundException;
import com.ecommerce.repositories.user.IUserRepository;
import com.ecommerce.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserModel user = this.repository.findByEmail(username).orElseThrow(() -> new EventNotFoundException(""));
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}