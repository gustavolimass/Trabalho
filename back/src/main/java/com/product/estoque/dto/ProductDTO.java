package com.product.estoque.dto;

public record ProductDTO(
        Integer id,
        String name,
        Integer quantity,
        CategoryDTO categoryId
) {
}
