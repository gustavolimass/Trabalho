package com.product.estoque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*") 
public class ProdutoController { 

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public List<Product> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    @PostMapping
    public Product adicionarProduto(@RequestBody Product produto) {
        return produtoRepository.save(produto);
    }

    @PutMapping("/{id}")
    public Product atualizarProduto(@PathVariable Long id, @RequestBody Product produtoDetalhes) {
        Product produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setNome(produtoDetalhes.getNome());
        produto.setQuantidade(produtoDetalhes.getQuantidade());

        return produtoRepository.save(produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}