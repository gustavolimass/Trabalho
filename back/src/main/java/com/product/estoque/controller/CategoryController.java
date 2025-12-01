package com.product.estoque.controller;

import com.product.estoque.dto.CategoryCreateDTO;
import com.product.estoque.dto.CategoryDTO;
import com.product.estoque.dto.CategoryUpdateDTO;
import com.product.estoque.entity.Category;
import com.product.estoque.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryDTO createCategory(@RequestBody CategoryCreateDTO dto) {
        Category category = new Category(dto.name());
        categoryService.createCategory(category);
        return  new CategoryDTO(category.getId(), category.getName());
    }

    @GetMapping
    public List<CategoryDTO> listAll() {
        List<Category> categories = categoryService.findAllCategories();
        return  categories.stream().map(category -> new CategoryDTO(category.getId(), category.getName())).toList();
    }

    @PutMapping("/{id}")
    public CategoryDTO updateCategory(@PathVariable Integer id, @RequestBody CategoryUpdateDTO dto) {
        Category category = new Category(dto.name());
        Category updatedCategory = categoryService.updateCategory(id, category);

        return new CategoryDTO(updatedCategory.getId(), updatedCategory.getName());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}
