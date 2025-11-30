package com.product.estoque;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private UserType tipoUsuario;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public UserType getTipoUsuario() { return tipoUsuario; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setTipoUsuario(UserType tipoUsuario) { this.tipoUsuario = tipoUsuario; }
}