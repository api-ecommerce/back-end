package com.ecommerce.dtos.user;

public record RegisterUserRequestDTO(String name, String cpf, String email, String password) {
}
