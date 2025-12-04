package com.product.estoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductCreateDTO(
        @NotBlank(message = "O nome não pode ser vazio")
        String name,

        @NotNull(message = "A quantidade não pode ser nula")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantity,

        @NotNull(message = "A categoria é obrigatória")
        CategoryDTO categoryId
) {
}
