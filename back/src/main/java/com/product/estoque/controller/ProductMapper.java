package com.product.estoque.mapper;

import com.product.estoque.dto.CategoryDTO;
import com.product.estoque.dto.ProductCreateDTO;
import com.product.estoque.dto.ProductDTO;
import com.product.estoque.entity.Category;
import com.product.estoque.entity.Product;

public class ProductMapper {

    public static ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        CategoryDTO categoryDTO = new CategoryDTO(
                product.getCategory().getId(),
                product.getCategory().getName()
        );
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getQuantity(),
                categoryDTO
        );
    }

    public static Product toEntity(ProductCreateDTO dto) {
        return new Product(dto.name(), dto.quantity(), new Category(dto.categoryId().id(), dto.categoryId().name()));
    }
}