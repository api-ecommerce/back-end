package com.ecommerce.entities.product;

import com.ecommerce.dtos.product.RegisterProductRequestDTO;
import com.ecommerce.entities.category.CategoryModel;
import com.ecommerce.entities.user.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_product")
public class ProductModel {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryModel categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price_in_cents", nullable = false)
    private int priceInCents;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image")
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private UserModel createdBy;

    @Column(name = "created_on", nullable = false)
    private Date createdOn = new Date();

    public ProductModel(RegisterProductRequestDTO data, CategoryModel category) throws IOException{
        this.name = data.name();
        this.description = data.description();
        this.priceInCents = data.priceInCents();
        this.stockQuantity = data.stockQuantity();
        this.sku = data.sku();
        this.image = data.image().getBytes();
        this.categoryId = category;
    }

}
