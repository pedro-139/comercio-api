package com.bricks.productos_api.service;


import com.bricks.productos_api.dto.CategoriaDTO;
import java.util.List;

public interface CategoriaService {


    List<CategoriaDTO> getAll();

    CategoriaDTO findById(Long id);

}
