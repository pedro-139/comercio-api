package com.bricks.productos_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    private Long id;


    @NotBlank(message = "El nombre de la categoria no puede estar vacío")
    @Size(min = 3, max = 75, message = "El nombre de la categoria debe tener entre 3y 75 caracteres")
    private String name;
}
