package com.product.estoque.dto;

public record ProductCreateDTO(
        String name,
        Integer quantity,
        CategoryDTO categoryId
) {
}
