package com.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;


@Controller
public class UserController {

    public ResponseEntity helloWorld(){
        return ResponseEntity.ok("Hello World");
    }

    public ResponseEntity getByUser(){
        return ResponseEntity.ok("teste");
    }
}
