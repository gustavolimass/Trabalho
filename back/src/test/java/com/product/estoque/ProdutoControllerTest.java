package com.product.estoque;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import com.product.estoque.controller.ProductController;
import com.product.estoque.entity.Category;
import com.product.estoque.entity.Product;
import com.product.estoque.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void deveListarProdutosCorretamente() throws Exception {
        // CENÁRIO
        Product p1 = new Product();
        p1.setId(1);
        p1.setName("Notebook Teste");
        p1.setQuantity(10);
        p1.setCategory(new Category(1,"Eletrônico"));

        List<Product> listaFalsa = List.of(p1);

        when(productService.findAllProducts()).thenReturn(listaFalsa);

        // TESTE
        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Notebook Teste"))
                .andExpect(jsonPath("$[0].quantity").value(10));
    }

    @Test
    public void deveAdicionarProdutoCorretamente() throws Exception {
        // CENÁRIO

        Product novoProduto = new Product();
        novoProduto.setId(8);
        novoProduto.setName("Mouse Gamer");
        novoProduto.setQuantity(50);
        novoProduto.setCategory(new Category(1, "Periférico"));

        when(productService.createProduct(org.mockito.ArgumentMatchers.any(Product.class)))
                .thenReturn(novoProduto);

        System.out.println(objectMapper.writeValueAsString(novoProduto));

        // TESTE
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoProduto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mouse Gamer"));
    }
}
