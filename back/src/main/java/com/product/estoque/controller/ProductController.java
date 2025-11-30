package com.product.estoque.controller;

import com.product.estoque.dto.CategoryDTO;
import com.product.estoque.dto.ProductCreateDTO;
import com.product.estoque.dto.ProductDTO;
import com.product.estoque.dto.ProductUpdateDTO;
import com.product.estoque.entity.Category;
import com.product.estoque.entity.Product;
import com.product.estoque.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        Product product = new Product(productCreateDTO.name(), productCreateDTO.quantity(), new Category(productCreateDTO.categoryId().id(), productCreateDTO.categoryId().name()));
        Product saved = productService.createProduct(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ProductDTO(
                                saved.getId(),
                                saved.getName(),
                                saved.getQuantity(),
                                new CategoryDTO(
                                        saved.getCategory().getId(),
                                        saved.getCategory().getName())));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listAll () {
        List<Product> products = productService.findAllProducts();
        return ResponseEntity.ok(products
                .stream()
                .map(product ->
                        new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getQuantity(),
                                new CategoryDTO(
                                        product.getCategory().getId(),
                                        product.getCategory().getName())))
                .toList());
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ProductDTO>> listByCategory(@RequestParam Integer categoryId) {
        List<Product> products = productService.findByCategory(categoryId);
        return ResponseEntity.ok(products
                .stream()
                .map(product ->
                        new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getQuantity(),
                                new CategoryDTO(
                                        product.getCategory().getId(),
                                        product.getCategory().getName())))
                .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @RequestBody ProductUpdateDTO productUpdateDTO) {
        Product product = new Product(productUpdateDTO.name(), productUpdateDTO.quantity(), new Category(productUpdateDTO.categoryId().id(), productUpdateDTO.categoryId().name()));
        Product updatedProduct = productService.updateProduct(id, product);

        return ResponseEntity.ok(
                new ProductDTO(
                        updatedProduct.getId(),
                        updatedProduct.getName(),
                        updatedProduct.getQuantity(),
                        new CategoryDTO(
                                updatedProduct.getCategory().getId(),
                                updatedProduct.getCategory().getName())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct (@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
