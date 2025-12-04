package com.product.estoque;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.estoque.controller.ProductController;
import com.product.estoque.dto.CategoryDTO;
import com.product.estoque.dto.ProductCreateDTO;
import com.product.estoque.entity.Category;
import com.product.estoque.entity.Product;
import com.product.estoque.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1. @WebMvcTest(ProductController.class) foca o teste apenas na camada web para o controller especificado.
// É mais rápido e focado que carregar o contexto Spring inteiro.
@WebMvcTest(ProductController.class)
public class ProdutoControllerTest {

    // 2. @Autowired MockMvc é o principal ponto de entrada para simular requisições HTTP (GET, POST, etc.).
    @Autowired
    private MockMvc mockMvc;

    // 3. @MockBean cria um mock do ProductService. O Spring vai injetar esse mock no ProductController.
    // Esta é a correção principal para evitar o erro de NullPointerException.
    @MockBean
    private ProductService productService;

    // 4. ObjectMapper é um utilitário para converter objetos Java em JSON e vice-versa.
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        // --- ARRANGE (Organizar) ---
        // a. Criamos o DTO que será enviado no corpo da requisição.
        // Corrigindo a instanciação do DTO: fornecemos o ID e null para o nome,
        // pois o construtor do record espera ambos os parâmetros.
        CategoryDTO categoryIdDto = new CategoryDTO(1, null); // Correção aqui
        ProductCreateDTO productCreateDTO = new ProductCreateDTO("Notebook Gamer", 10, categoryIdDto);

        // b. Criamos o objeto Product que esperamos que o serviço retorne após salvar.
        Category category = new Category(1, "Eletrônicos");
        Product productSalvo = new Product("Notebook Gamer", 10, category);
        productSalvo.setId(1); // O produto salvo no banco teria um ID.

        // c. Configuramos o comportamento do nosso mock:
        when(productService.createProduct(any(Product.class))).thenReturn(productSalvo);

        // --- ACT & ASSERT (Agir e Verificar) ---
        mockMvc.perform(post("/api/product") // Simula uma requisição POST para o endpoint correto
                .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo como JSON
                .content(objectMapper.writeValueAsString(productCreateDTO))) // Converte o DTO para uma string JSON
                .andExpect(status().isCreated()) // Esperamos que o status da resposta seja 201 (Created)
                .andExpect(jsonPath("$.id").value(1)) // Verifica se o JSON de resposta tem o campo "id" com valor 1
                .andExpect(jsonPath("$.name").value("Notebook Gamer")); // Verifica o nome
    }
}