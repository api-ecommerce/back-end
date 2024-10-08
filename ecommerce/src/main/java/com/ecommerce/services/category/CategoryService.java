package com.ecommerce.services.category;

import com.ecommerce.entities.category.CategoryModel;
import com.ecommerce.repositories.category.ICategoryRepository;
import com.ecommerce.services.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final ICategoryRepository repository;

    private final UserService userService;

    public CategoryService(ICategoryRepository repository, UserService userService){
        this.repository = repository;
        this.userService = userService;
    }
}
