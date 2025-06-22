package com.tinysteps.tinysteps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinysteps.tinysteps.model.CategoryModel;
import com.tinysteps.tinysteps.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService CategoryService;

    @GetMapping("")
    public List<CategoryModel> getAllCategory() {
        return CategoryService.getAllCategory();
    }

    @PostMapping("/add")
    public String addCategory(@RequestBody CategoryModel categoryModel) {
        return CategoryService.addCategory(categoryModel.getName());
    }

    @PutMapping("update/{id}")
    public String editCategory(@RequestBody CategoryModel categoryModel) {
        return CategoryService.editCategory(categoryModel);
    }

    @DeleteMapping("delete/{id}")
    public String deleteCategory(@PathVariable String id) {
        Long categoryId = Long.valueOf(id);
        return CategoryService.deleteCategory(categoryId);
    }
}
