package com.product.estoque.service;

import com.product.estoque.entity.Product;
import com.product.estoque.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct (Product product) {

        productRepository.findByName(product.getName())
                .ifPresent(p -> {
                    throw new RuntimeException("Já existe um produto cadastrado com o nome: " + p.getName());
                });

        return productRepository.save(product);
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> findByCategory(Integer id) {
        List<Product> products = productRepository.findByCategoryId(id);

        if (products.isEmpty()) {
            throw new RuntimeException("Nenhum produto encontrado para a categoria ID: " + id);
        }

        return products;
    }

    public Product updateProduct(Integer id, Product product) {
        Product productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nenhum produto encontrado para a categoria ID: " + id));

        Product productUpdated = new Product();
        productUpdated.setId(productEntity.getId());
        productUpdated.setName(
                product.getName() != null ? product.getName() : productEntity.getName()
        );
        productUpdated.setQuantity(
                product.getQuantity() != null ? product.getQuantity() : productEntity.getQuantity()
        );
        productUpdated.setCategory( product.getCategory() != null ? product.getCategory() : productEntity.getCategory());
        return productRepository.save(productUpdated);
    }

    public void delete(Integer id) {
        productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nenhum produto encontrado com o id: " + id));

        productRepository.deleteById(id);
    }

}
