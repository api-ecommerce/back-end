package com.ecommerce.controllers.product;

import com.ecommerce.dtos.product.ProductResponseDTO;
import com.ecommerce.dtos.product.RegisterProductRequestDTO;
import com.ecommerce.dtos.product.UpdateProductRequestDTO;
import com.ecommerce.services.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    @RequestMapping("/{sku}")
    public ResponseEntity findBySku(@PathVariable String sku){
        return productService.findBySku(sku);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProduct(){
        return productService.getAllProduct();
    }

    @PostMapping
    @RequestMapping("/register")
    public ResponseEntity registerProduct(@RequestBody RegisterProductRequestDTO request){
        return productService.registerProduct(request);
    }

    @PutMapping
    @RequestMapping("/update/{sku}")
    public ResponseEntity updateProduct(@PathVariable String sku, @RequestBody UpdateProductRequestDTO request){
        return productService.updateProduct(sku, request);
    }

    @DeleteMapping
    @RequestMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable String id){
        return productService.deleteProduct(id);
    }

    @GetMapping
    @RequestMapping("/{name}")
    public ResponseEntity searchProduct(@PathVariable String name){
        return productService.searchProduct(name);
    }
}
