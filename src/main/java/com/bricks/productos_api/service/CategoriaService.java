package com.bricks.productos_api.service;


import com.bricks.productos_api.model.Categoria;

import java.util.List;

public interface CategoriaService {


    List<Categoria> getAll() throws Exception;

    Categoria findById(Long id) throws Exception;

}