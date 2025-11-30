package com.product.estoque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_product")

public class Product {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String name, Integer quantity, Category category) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }

    public Product(Integer id, String name, Integer quantity, Category category) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }

    public Product() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}