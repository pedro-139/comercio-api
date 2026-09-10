package com.bricks.productos_api.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    @Id
    private Long id;

    @Column (name = "nombre", nullable = false, length = 100)
    private String name;
}
