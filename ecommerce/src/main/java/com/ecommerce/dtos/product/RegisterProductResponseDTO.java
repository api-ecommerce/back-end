package com.ecommerce.dtos.product;

import org.springframework.http.HttpStatus;

public record RegisterProductResponseDTO(HttpStatus status, String message) {
}
