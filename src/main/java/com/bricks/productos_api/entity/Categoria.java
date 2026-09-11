package com.bricks.productos_api.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table( name = "categorias")
public class Categoria {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String name;

    // Constructores

    public Categoria() {}
    public Categoria(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
