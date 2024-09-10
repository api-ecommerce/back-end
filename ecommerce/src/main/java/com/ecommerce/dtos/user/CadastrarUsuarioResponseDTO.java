package com.ecommerce.dtos.user;

import org.springframework.http.HttpStatus;

public record CadastrarUsuarioResponseDTO(HttpStatus status, String mensagem) {}
