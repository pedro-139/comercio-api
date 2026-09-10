package com.bricks.productos_api.controller;

import com.bricks.productos_api.dto.CategoriaDTO;
import com.bricks.productos_api.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")

public class CategoriaController  {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /*
    El ResponseEntity<?> es una clase que representa todas las respuestas HTTP y '?' es un tipo de dato generico por lo que
    la respuestas puede ser de cualquier tipo.
     */

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        List<CategoriaDTO> categorias = categoriaService.getAll();
        return ResponseEntity.ok(categorias);
    }


}

