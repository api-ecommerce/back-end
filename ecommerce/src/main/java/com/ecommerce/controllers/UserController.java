package com.ecommerce.controllers;

import com.ecommerce.dtos.user.*;
import com.ecommerce.entities.user.UserModel;
import com.ecommerce.infra.security.TokenService;
import com.ecommerce.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @RequestMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegisterUserRequestDTO request) {
        return userService.registerUser(request);
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO request) {
        return userService.login(request);
    }


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping
    @RequestMapping("/{email}")
    public ResponseEntity findByEmail(@PathVariable String email){
        return userService.findByEmail(email);
    }

    @PostMapping
    @RequestMapping("/forgot-password")
    public ResponseEntity forgotPassword(@RequestBody ForgotPasswordRequestDTO request){
        return userService.forgotPassword(request);
    }

    @PatchMapping
    @RequestMapping("/edit-password/{email}")
    public ResponseEntity editPassword(@PathVariable String email, @RequestBody EditPasswordRequestDTO request){
        return userService.editPassword(request, email);
    }

    @PatchMapping
    @RequestMapping("/permission/{email}")
    public ResponseEntity editAdminPermission(@PathVariable String email, @RequestBody UserEditTypeRequestDTO request){
        return userService.editAdminPermission(email, request);
    }

    @PutMapping
    @RequestMapping("/update/{email}")
    public ResponseEntity updateUser(@PathVariable String email, @RequestBody UpdateUserRequestDTO request){
        return userService.updateUser(email, request);
    }

    @DeleteMapping
    @RequestMapping("/delete/{email}")
    public ResponseEntity deleteUser(@PathVariable String email){
        return userService.deleteUser(email);
    }

    @GetMapping("/hello")
    public ResponseEntity<UserModel> helloWorld() {
        UserModel teste = getAuthenticatedUser();
        return ResponseEntity.ok(teste);
    }

    public UserModel getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserModel) authentication.getPrincipal();
    }
}
