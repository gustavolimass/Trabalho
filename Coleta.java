package com.product.estoque;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coletas")
public class Coleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Product produto;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private int quantidadeColetada;

    private LocalDateTime dataHoraColeta;

    public Long getId() { return id; }
    public Product getProduto() { return produto; }
    public Usuario getUsuario() { return usuario; }
    public int getQuantidadeColetada() { return quantidadeColetada; }
    public LocalDateTime getDataHoraColeta() { return dataHoraColeta; }

    public void setId(Long id) { this.id = id; }
    public void setProduto(Product produto) { this.produto = produto; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setQuantidadeColetada(int quantidade) { this.quantidadeColetada = quantidade; }
    public void setDataHoraColeta(LocalDateTime dataHora) { this.dataHoraColeta = dataHora; }
}