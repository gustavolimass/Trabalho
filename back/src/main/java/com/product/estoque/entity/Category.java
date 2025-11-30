package com.product.estoque.entity;

import jakarta.persistence.*;

    @Entity
    @Table(name = "tb_category")
    public class Category {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "name")
        private String name;

        public Category(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Category(String name) {
            this.name = name;
        }

        public Category() {

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
    }

