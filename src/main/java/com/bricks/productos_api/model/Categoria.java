package com.bricks.productos_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @Column (name = "nombre", nullable = false, length = 100)
    private String name;
}
