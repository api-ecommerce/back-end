package com.ecommerce.dtos.user;

import org.springframework.http.HttpStatus;

public record RegisterUserResponseDTO(HttpStatus status, String mensagem) {}
