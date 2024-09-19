package com.ecommerce.dtos.user;

import com.ecommerce.entities.user.UserRole;

public record UserResponseDTO(String name, String cpf, String email, UserRole role) {
}
