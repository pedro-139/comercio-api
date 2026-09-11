package com.bricks.productos_api.dto.producto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequest {
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor o igual a 0")
    private Double price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 1, message = "El stock debe ser mayor o igual a 1")
    private Integer stock;

    @NotNull(message = "El ID de categoría es obligatorio")
    private Long categoryId;
}
