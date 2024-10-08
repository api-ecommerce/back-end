package com.ecommerce.services.product;


import com.ecommerce.dtos.product.*;
import com.ecommerce.entities.category.CategoryModel;
import com.ecommerce.entities.product.ProductModel;
import com.ecommerce.entities.user.UserModel;
import com.ecommerce.exceptions.EventInternalServerErrorException;
import com.ecommerce.exceptions.EventNotFoundException;
import com.ecommerce.repositories.category.ICategoryRepository;
import com.ecommerce.repositories.product.IProductRepository;
import com.ecommerce.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final IProductRepository repository;

    private final ICategoryRepository categoryRepository;

    private final UserService userService;

    public ProductService(IProductRepository repository, ICategoryRepository categoryRepository, UserService userService){
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    ProductModel product = new ProductModel();
    CategoryModel category = new CategoryModel();
    UserModel user = new UserModel();

    private ProductModel validateProduct(String sku){
        return repository.findBySku(sku).orElseThrow(() -> new EventNotFoundException("Produto não encontrado."));
    }

    public ResponseEntity<ProductResponseDTO> findBySku(String sku){
        try{
            product = validateProduct(sku);

            String categoryId = product.getCategoryId().getId();
            String createdById = product.getCreatedBy().getId();

            ProductResponseDTO response = new ProductResponseDTO(
                    product.getId(),
                    product.getName(),
                    categoryId,
                    product.getDescription(),
                    product.getPriceInCents(),
                    product.getStockQuantity(),
                    product.getSku(),
                    product.getImage(),
                    createdById,
                    product.getCreatedOn()
            );

            return new ResponseEntity(response, HttpStatus.OK);
        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }


    public ResponseEntity<List<ProductResponseDTO>> getAllProduct(){
        try {
            List<ProductModel> products = repository.findAll();
            List<ProductResponseDTO> response = products.stream()
                    .map(product -> new ProductResponseDTO(
                            product.getId(),
                            product.getName(),
                            product.getCategoryId().getId(),
                            product.getDescription(),
                            product.getPriceInCents(),
                            product.getStockQuantity(),
                            product.getSku(),
                            product.getImage(),
                            product.getCreatedBy().getId(),
                            product.getCreatedOn()
                    ))
                    .collect(Collectors.toList());

            return new ResponseEntity(response, HttpStatus.OK);
        }catch (RuntimeException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }


    public ResponseEntity registerProduct(RegisterProductRequestDTO request){
        try{
            if(repository.findBySku(request.sku()).isPresent()){
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new RegisterProductResponseDTO(HttpStatus.CONFLICT, "Produto com este codigo SKU já existe."));
            }

            category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new EventNotFoundException("Categoria com Id" + request.categoryId() + "não encontrada."));

            product = new ProductModel(request, category);
            user = userService.getAuthenticatedUser();
            product.setCreatedBy(user);

            repository.save(product);

            return new ResponseEntity(new RegisterProductResponseDTO(HttpStatus.CREATED, "Produto criado com sucesso."), HttpStatus.CREATED);
        }catch (IOException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity updateProduct(String sku, UpdateProductRequestDTO request){
        try{
            //Verifica se o sku existe
            Optional<ProductModel> productOptional = repository.findBySku(sku);
            if (productOptional.isEmpty()) {
                return new ResponseEntity<>(new UpdateProductResponseDTO(HttpStatus.NOT_FOUND, "Produto não encontrado."), HttpStatus.NOT_FOUND);
            }

            product = productOptional.get();

            //Verifica se o SKU é unico
            if (!product.getSku().equals(request.sku())) {
                Optional<ProductModel> skuConflict = repository.findBySku(request.sku());
                if (skuConflict.isPresent()) {
                    return new ResponseEntity<>(new UpdateProductResponseDTO(HttpStatus.CONFLICT, "O código SKU fornecido já está em uso por outro produto. Insira um código único."), HttpStatus.CONFLICT);
                }
            }

            product.setName(request.name());
            product.setDescription(request.description());
            product.setPriceInCents(request.priceInCents());
            product.setStockQuantity(request.stockQuantity());
            product.setImage(request.image().getBytes());


            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new EventNotFoundException("Categoria não encontrada"));

            product.setCategoryId(category);

            repository.save(product);

            return new ResponseEntity(new UpdateProductResponseDTO(HttpStatus.OK, "Produto atualizado com sucesso."), HttpStatus.OK);
        } catch (IOException ex) {
            throw new EventInternalServerErrorException("Erro ao processar a imagem: " + ex.getMessage());
        } catch (Exception ex) {
            throw new EventInternalServerErrorException("Erro interno ao atualizar o produto: " + ex.getMessage());
        }
    }

    public ResponseEntity deleteProduct (String id){
        try{
            repository.findById(id).orElseThrow(() -> new EventNotFoundException("Produto não encontrado."));
            repository.deleteById(id);
            return new ResponseEntity<>("Produto deletado com sucesso", HttpStatus.OK);
        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }


    public ResponseEntity<List<SearchProductResponseDTO>> searchProduct(String name) {
        try {
            List<ProductModel> products = repository.findByName(name);

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonList(new SearchProductResponseDTO("Produto não encontrado", null, null, 0, 0, null, null)));
            }

            // Usando Stream para mapear os produtos para DTOs
            List<SearchProductResponseDTO> response = products.stream()
                    .map(productModel -> new SearchProductResponseDTO(
                            productModel.getName(),
                            productModel.getCategoryId().getName(),
                            productModel.getDescription(),
                            productModel.getPriceInCents(),
                            productModel.getStockQuantity(),
                            productModel.getSku(),
                            productModel.getImage()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (JpaSystemException ex) {
            throw new EventInternalServerErrorException("Erro no sistema ao acessar o banco de dados: " + ex.getMessage());
        } catch (RuntimeException ex) {
            throw new RuntimeException("Erro inesperado: " + ex.getMessage());
        }
    }



    /*Obter Produtos por Categoria*/
    /*desativarProduto*/
    /*reativarProduto*/

}
