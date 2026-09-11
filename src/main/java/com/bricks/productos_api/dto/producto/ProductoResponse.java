package com.bricks.productos_api.dto.producto;

import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponse {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private CategoriaResponse category;

}
