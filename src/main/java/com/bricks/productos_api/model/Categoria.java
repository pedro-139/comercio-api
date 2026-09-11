package com.bricks.productos_api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "categorias")
public class Categoria {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String name;
}
