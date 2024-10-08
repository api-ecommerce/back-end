package com.ecommerce.repositories.category;

import com.ecommerce.entities.category.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryModel, String> {
}
