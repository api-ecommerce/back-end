package com.ecommerce.repositories.product;

import com.ecommerce.entities.product.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<ProductModel, String> {
    Optional<ProductModel> findBySku(String sku);
    List<ProductModel> findByName(String name);
}
