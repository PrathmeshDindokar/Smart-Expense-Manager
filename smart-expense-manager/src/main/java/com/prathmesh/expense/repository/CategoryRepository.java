package com.prathmesh.expense.repository;

import com.prathmesh.expense.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {
    List<Category>findByType(String type);
    List<Category>findByNameContainingIgnoreCase(String name);
}