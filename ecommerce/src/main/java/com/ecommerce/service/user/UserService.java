package com.ecommerce.service.user;

import com.ecommerce.entities.user.UserModel;
import com.ecommerce.exceptions.EventNotFoundException;
import com.ecommerce.repositories.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {

    @Autowired
    IUserRepository repository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserModel validarEmail(String email){
        return repository.findByEmail(email).orElseThrow(() -> new EventNotFoundException("Usuário não encontrado."));
    }
}
