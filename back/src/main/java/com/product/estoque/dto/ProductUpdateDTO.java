package com.product.estoque.dto;

public record ProductUpdateDTO(
        String name,
        Integer quantity,
        CategoryDTO categoryId
) {
}
