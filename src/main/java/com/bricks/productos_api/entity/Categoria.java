package com.bricks.productos_api.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    @Id
    private Long id;

    @Column (name = "nombre", nullable = false, length = 50)
    private String name;
}
