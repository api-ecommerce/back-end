package com.ecommerce.dtos.product;

public record SearchProductResponseDTO(String name, String categoryName, String description, int priceInCents, int stockQuantity, String sku, byte[] image) {
}
