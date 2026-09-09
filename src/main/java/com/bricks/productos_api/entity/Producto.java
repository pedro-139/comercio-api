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
    @Column(nullable = false, name ="nombre", length = 100) // La columna no puede ser nula, debe llamarse "nombre" y tiene una cantidad máxima de 100 caracteres.
    private String name;


    //@DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false, name ="precio")
    private Double price;

    //@Min(0)
    @Column(nullable = false)
    private int stock;

   // @Valid
   // @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Categoria category; // Deberia ser Long o una categoria?
}
