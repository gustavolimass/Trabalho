package com.product.estoque.controller;

import com.product.estoque.entity.Product;
import com.product.estoque.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> listAll () {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<Product>> listByCategory(@RequestParam Integer categoryId) {
        return ResponseEntity.ok(productService.findByCategory(categoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct (@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}