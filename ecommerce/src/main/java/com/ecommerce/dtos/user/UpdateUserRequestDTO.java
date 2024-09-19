package com.ecommerce.dtos.user;

import java.util.UUID;

public record UpdateUserRequestDTO(String name, String cpf, String email) {
}
