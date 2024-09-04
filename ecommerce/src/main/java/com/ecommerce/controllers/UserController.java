package com.ecommerce.controllers;

import com.ecommerce.dtos.user.CadastrarUsuarioRequestDTO;
import com.ecommerce.dtos.user.LoginRequestDTO;
import com.ecommerce.entities.user.UserModel;
import com.ecommerce.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @RequestMapping("cadastro")
    public ResponseEntity cadastrarUsuario (@Valid @RequestBody CadastrarUsuarioRequestDTO request){
        return userService.cadastrarUsuario(request);
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO request){
        return userService.login(request);
    }

    @GetMapping
    @RequestMapping("/hello2")
    public ResponseEntity helloWorld(){
        UserModel teste = getAuthenticatedUser();
        return ResponseEntity.ok(teste);
    }

    public UserModel getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserModel
                ) authentication.getPrincipal();
    }

}
