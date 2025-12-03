package com.product.estoque;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.estoque.controller.ProductController;
import com.product.estoque.dto.CategoryDTO;
import com.product.estoque.dto.ProductCreateDTO;
import com.product.estoque.entity.Category;
import com.product.estoque.entity.Product;
import com.product.estoque.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO(1, "Informática");
        ProductCreateDTO createDTO = new ProductCreateDTO("Notebook Gamer", 10, categoryDTO);

        Product saved = new Product("Notebook Gamer", 10, new Category(1, "Informática"));
        saved.setId(1);

        when(productService.createProduct(any(Product.class))).thenReturn(saved);

        String body = objectMapper.writeValueAsString(createDTO);

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Notebook Gamer"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.category.name").value("Informática"));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).createProduct(productCaptor.capture());
        Product enviado = productCaptor.getValue();

        assertThat(enviado.getName()).isEqualTo("Notebook Gamer");
        assertThat(enviado.getQuantity()).isEqualTo(10);
        assertThat(enviado.getCategory().getId()).isEqualTo(1);
        assertThat(enviado.getCategory().getName()).isEqualTo("Informática");
    }

    @Test
    void deveListarTodosOsProdutos() throws Exception {
        // Arrange: Crie os dados de teste
        Category category1 = new Category(1, "Informática");
        Product product1 = new Product("Notebook Gamer", 10, category1);
        product1.setId(1);

        Category category2 = new Category(2, "Periféricos");
        Product product2 = new Product("Mouse sem fio", 50, category2);
        product2.setId(2);

        List<Product> productList = List.of(product1, product2);

        // Mock: Simule o comportamento do serviço
        when(productService.findAllProducts()).thenReturn(productList);

        // Act & Assert: Execute a requisição e verifique o resultado
        mockMvc.perform(get("/api/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Notebook Gamer"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Mouse sem fio"))
                .andExpect(jsonPath("$[1].category.name").value("Periféricos"));
    }
}
