package com.bricks.productos_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
    @Column(nullable = false, name ="nombre", length = 100)
    private String name;



    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @Column(nullable = false, name ="precio")
    private Double price;


    @Column(nullable = false)
    private int stock;


    @ManyToOne //Muchos productos pertenecen a una categoria y una categoria puede tener muchos productos.
    @JoinColumn(name = "categoria", nullable = false, referencedColumnName = "id")
    private Categoria category; // Deberia ser Long o una categoria?


}
