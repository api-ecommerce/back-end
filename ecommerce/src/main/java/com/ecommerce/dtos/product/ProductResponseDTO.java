package com.ecommerce.dtos.product;

import com.ecommerce.entities.category.CategoryModel;
import com.ecommerce.entities.user.UserModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public record ProductResponseDTO(String id, String name, String categoryId, String description, int priceInCents, int stockQuantity, String sku, byte[] image, String createdBy, Date createdOn) {
}
