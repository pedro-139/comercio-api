package com.bricks.productos_api.service;


import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import com.bricks.productos_api.entity.Categoria;

import java.util.List;


public interface CategoriaService {


    List<CategoriaResponse> getAll();

    Categoria getCategoriaById(Long id);
    CategoriaResponse findById(Long id);

}
