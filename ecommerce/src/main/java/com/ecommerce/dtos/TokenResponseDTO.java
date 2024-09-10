package com.ecommerce.dtos;

public record TokenResponseDTO(String email, String token, boolean status) { }