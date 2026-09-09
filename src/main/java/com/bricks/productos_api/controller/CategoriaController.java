package com.bricks.productos_api.controller;

import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.entity.Producto;
import com.bricks.productos_api.service.CategoriaService;
import com.bricks.productos_api.service.ProductoService;
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

    /*
    El ResponseEntity<?> es una clase que representa todas las respuestas HTTP y '?' es un tipo de dato generico por lo que
    la respuestas puede ser de cualquier tipo.
     */

    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategorias(){
        List<Categoria> Categorias = categoriaService.listarCategorias();
        return ResponseEntity.ok(Categorias);
    }


}

