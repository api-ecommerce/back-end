package com.ecommerce.dtos.product;

import org.springframework.web.multipart.MultipartFile;

public record RegisterProductRequestDTO(String name, String description, int priceInCents, int stockQuantity, String sku, MultipartFile image, String categoryId ) {
}
