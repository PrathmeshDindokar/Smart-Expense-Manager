package com.prathmesh.expense.service;

import com.prathmesh.expense.entity.Category;
import com.prathmesh.expense.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public List<Category> getCategoriesByType(String type) {
        return categoryRepository.findByType(type);
    }
}