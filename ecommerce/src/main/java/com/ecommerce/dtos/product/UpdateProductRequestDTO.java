package com.ecommerce.dtos.product;

import com.ecommerce.entities.category.CategoryModel;
import org.springframework.web.multipart.MultipartFile;

public record UpdateProductRequestDTO(String name, String description, int priceInCents, int stockQuantity, String sku, MultipartFile image, String categoryId) {
}
