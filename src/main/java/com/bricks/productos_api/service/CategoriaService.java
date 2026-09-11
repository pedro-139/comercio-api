package com.bricks.productos_api.service;


import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import com.bricks.productos_api.model.Categoria;

import java.util.List;


public interface CategoriaService {


    List<CategoriaResponse> getAll();

    // Devuelve la entidad para simplificar la lógica de negocio posteriormente.
    Categoria getCategoriaById(Long id);

    CategoriaResponse findById(Long id);

}
