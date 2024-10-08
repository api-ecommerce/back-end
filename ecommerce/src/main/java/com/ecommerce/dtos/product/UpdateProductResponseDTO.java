package com.ecommerce.dtos.product;

import org.springframework.http.HttpStatus;

public record UpdateProductResponseDTO(HttpStatus status, String message) {
}
