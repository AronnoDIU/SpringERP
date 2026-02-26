package com.springerp.service;

import com.springerp.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);
    Category updateCategory(Long id, Category category);
    void deleteCategory(Long id);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
}
