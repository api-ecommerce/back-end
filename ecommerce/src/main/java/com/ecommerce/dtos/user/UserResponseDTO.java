package com.ecommerce.dtos.user;

import com.ecommerce.entities.user.UserRole;

import java.util.Date;

public record UserResponseDTO(String id, String name, String cpf, String email, String password, UserRole role, boolean active, Date createdOn) {

}
