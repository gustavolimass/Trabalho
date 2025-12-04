package com.product.estoque.controller;

import com.product.estoque.dto.CategoryDTO;
import com.product.estoque.dto.ProductCreateDTO;
import com.product.estoque.dto.ProductDTO;
import com.product.estoque.dto.ProductUpdateDTO;
import com.product.estoque.entity.Category;
import com.product.estoque.entity.Product;
import com.product.estoque.mapper.ProductMapper;
import com.product.estoque.service.ProductService;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
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
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid ProductCreateDTO productCreateDTO) {
        Product product = ProductMapper.toEntity(productCreateDTO);
        Product saved = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toDTO(saved));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listAll () {
        List<Product> products = productService.findAllProducts();
        List<ProductDTO> dtos = products.stream().map(ProductMapper::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ProductDTO>> listByCategory(@RequestParam Integer categoryId) {
        List<Product> products = productService.findByCategory(categoryId);
        List<ProductDTO> dtos = products.stream().map(ProductMapper::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @RequestBody ProductUpdateDTO productUpdateDTO) {
        Product product = ProductMapper.toEntity(productUpdateDTO);
        Product updatedProduct = productService.updateProduct(id, product);

        return ResponseEntity.ok(ProductMapper.toDTO(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct (@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
