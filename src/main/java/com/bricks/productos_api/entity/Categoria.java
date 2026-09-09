package com.bricks.productos_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    @Id
    private Long id;
    private String name;
}
