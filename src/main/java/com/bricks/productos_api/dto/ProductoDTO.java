package com.bricks.productos_api.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Size(min = 3,max = 100, message = "El nombre del producto debe tener entre 3 y 100 caracteres")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")

    private Double price;


    @NotNull(message = "El stock es obligatorio")
    @Min(value = 1, message = "El stock debe ser mayor o igual a 1")
    private Integer stock;

    @NotNull(message = "El ID de categoría es obligatorio")
    private Long categoryId;
}
