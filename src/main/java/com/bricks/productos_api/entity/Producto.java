package com.bricks.productos_api.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Al registrar un producto el ID se incrementa solo.
    @Column(unique = true, nullable = false)
    private Long id;

    @NotBlank //La columna no puede estar vacía
    @Column(nullable = false, name ="nombre", length = 50) // La columna no puede ser nula, debe llamarse "nombre" y tiene una cantidad máxima de 50 caracteres.
    private String name;



    @Column(nullable = false, name ="precio")
    private Double price;


    @Column(nullable = false)
    private int stock;


    @ManyToOne //Muchos productos pertenecen a una categoria y una categoria puede tener muchos productos.
    @JoinColumn(name = "categoria", nullable = false, referencedColumnName = "id")
    private Categoria categoria; // Deberia ser Long o una categoria?
}
