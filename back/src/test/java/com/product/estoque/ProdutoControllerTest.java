//package com.product.estoque;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@WebMvcTest(ProdutoController.class)
//public class ProdutoControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProdutoRepository produtoRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void deveListarProdutosCorretamente() throws Exception {
//        // CENÁRIO
//        Product p1 = new Product();
//        p1.setId(1L);
//        p1.setNome("Notebook Teste");
//        p1.setQuantidade(10);
//
//        List<Product> listaFalsa = Arrays.asList(p1);
//
//        when(produtoRepository.findAll()).thenReturn(listaFalsa);
//
//        // TESTE
//        mockMvc.perform(get("/api/produtos"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].nome").value("Notebook Teste"))
//                .andExpect(jsonPath("$[0].quantidade").value(10));
//    }
//
//    @Test
//    public void deveAdicionarProdutoCorretamente() throws Exception {
//        // CENÁRIO
//        Product novoProduto = new Product();
//        novoProduto.setNome("Mouse Gamer");
//        novoProduto.setQuantidade(50);
//
//        when(produtoRepository.save(org.mockito.ArgumentMatchers.any(Product.class)))
//            .thenReturn(novoProduto);
//
//        // TESTE
//        mockMvc.perform(post("/api/produtos")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(novoProduto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.nome").value("Mouse Gamer"));
//    }
//}