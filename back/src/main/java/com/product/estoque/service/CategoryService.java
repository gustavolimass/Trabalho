package com.product.estoque.service;

import com.product.estoque.entity.Category;
import com.product.estoque.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {

        categoryRepository.findByName(category.getName())
                .ifPresent(c -> {
                    throw new RuntimeException("Já existe uma categoria com esse nome: " + c.getName());
                });

        return categoryRepository.save(category);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Integer id, Category category) {
        Category categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Category categoryUpdated = new Category();
        categoryUpdated.setId(categoryEntity.getId());
        categoryUpdated.setName(category.getName());

        return categoryRepository.save(categoryUpdated);
    }

    public void delete(Integer id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoryRepository.deleteById(id);
    }
}
