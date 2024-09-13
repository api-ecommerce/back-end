package com.ecommerce.controllers;

import com.ecommerce.dtos.user.*;
import com.ecommerce.entities.user.UserModel;
import com.ecommerce.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @RequestMapping("register")
    public ResponseEntity registerUser(@Valid @RequestBody RegisterUserRequestDTO request) {
        return userService.registerUser(request);
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO request) {
        return userService.login(request);
    }


    @GetMapping
    @RequestMapping("/get-users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping
    @RequestMapping("/get/{email}")
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
    @RequestMapping("/edit-type/{email}")
    public ResponseEntity editAdminPermission(@PathVariable String email, @RequestBody UserEditTypeRequestDTO request){
        return userService.editAdminPermission(email, request);
    }

    @GetMapping
    @RequestMapping("/hello")
    public ResponseEntity helloWorld() {
        UserModel teste = getAuthenticatedUser();
        return ResponseEntity.ok(teste);
    }

    public UserModel getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserModel) authentication.getPrincipal();
    }
}
