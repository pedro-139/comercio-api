package com.bricks.productos_api.dto;

import com.bricks.productos_api.entity.Categoria;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductoDTO {


    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Size(max = 100, message = "El nombre del producto no debe superar los 100 caracteres")
    private String name;

    @NotNull(message = "El campo es obligatorio")
    @Min(value = 0, message = "El precio debe ser mayor o igual a cero" )
    private Double price;

    @NotNull(message = "El campo es obligatorio")
    @Min(value = 1, message = "El stock debe ser al menos 1" )
    private int stock;


    private Categoria category;
}
