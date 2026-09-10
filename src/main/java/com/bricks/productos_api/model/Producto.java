package com.bricks.productos_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, name ="nombre", length = 100)
    private String name;


    @Column(nullable = false, name ="precio")
    private Double price;


    @Column(nullable = false)
    private Integer stock;


    @ManyToOne
    @JoinColumn(name = "categoria", nullable = false, referencedColumnName = "id")
    private Categoria category;


}
