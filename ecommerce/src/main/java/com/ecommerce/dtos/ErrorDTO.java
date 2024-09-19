package com.ecommerce.dtos;

import org.springframework.http.HttpStatus;

public record ErrorDTO(HttpStatus error, int code, String message, boolean status) { }