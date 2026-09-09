package com.bricks.productos_api.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    @JsonAlias("id")
    private Long idCategoria;

    @Column (name = "name", nullable = false, length = 50)
    private String name;
}
