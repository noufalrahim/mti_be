package com.tinysteps.tinysteps.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinysteps.tinysteps.model.CategoryModel;
import com.tinysteps.tinysteps.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryModel> getAllCategory() {
        return categoryRepository.findAll();
    }

    public String addCategory(String name) {
        Optional<CategoryModel> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent()) {
            return "Category already exists!";
        }

        CategoryModel newCategory = new CategoryModel();
        newCategory.setName(name);
        categoryRepository.save(newCategory);

        return "Category added successfully!";
    }

    public String editCategory(CategoryModel categoryModel) {
        Long id = categoryModel.getId();
        Optional<CategoryModel> existingCategory = categoryRepository.findById(id);

        if (!existingCategory.isPresent()) {
            return "No category exists with the given ID!";
        }

        categoryRepository.save(categoryModel);
        return "Category updated successfully!";
    }

    public String deleteCategory(Long id) {
        Optional<CategoryModel> existingCategory = categoryRepository.findById(id);

        if (!existingCategory.isPresent()) {
            return "No category exists with the given ID!";
        }

        categoryRepository.deleteById(id);
        return "Category deleted successfully!";
    }
}
