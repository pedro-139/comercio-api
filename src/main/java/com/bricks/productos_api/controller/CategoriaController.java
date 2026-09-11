package com.bricks.productos_api.controller;

import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import com.bricks.productos_api.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/categories")
public class CategoriaController  {
    @Autowired
    private CategoriaService categoriaService;


    @Operation(
            summary = "Listar categorias",
            description = "Devuelve todas las categorías creadas en el sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = " Listado de categorías devuelto exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.getAll());
    }


}

