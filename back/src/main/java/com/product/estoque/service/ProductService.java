package com.product.estoque.service;

import com.product.estoque.entity.Product;
import com.product.estoque.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Retorna uma lista com todos os produtos cadastrados.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Cria um novo produto, validando se já não existe um com o mesmo nome.
     */
    public Product createProduct(Product product) {
        productRepository.findByName(product.getName()).ifPresent(p -> {
            throw new RuntimeException("Já existe um produto cadastrado com o nome: " + product.getName());
        });
        return productRepository.save(product);
    }

    /**
     * Atualiza um produto existente.
     */
    public Product updateProduct(Integer id, Product productDetails) {
        // Busca o produto no banco de dados. Se não encontrar, lança uma exceção.
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o id: " + id));

        // Atualiza os campos do produto encontrado com os novos detalhes.
        product.setName(productDetails.getName());
        product.setQuantity(productDetails.getQuantity());
        // A categoria também poderia ser atualizada se necessário.
        // product.setCategory(productDetails.getCategory());

        // Salva o produto atualizado no banco de dados.
        return productRepository.save(product);
    }

    /**
     * Busca produtos por um ID de categoria.
     */
    public List<Product> findByCategory(Integer categoryId) {
        // Assumindo que o repositório terá um método para buscar por ID de categoria.
        // Este método precisa ser criado na interface ProductRepository.
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Deleta um produto pelo seu ID.
     */
    public void delete(Integer id) {
        productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o id: " + id));
        productRepository.deleteById(id);
    }
}