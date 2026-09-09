package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.repository.CategoriaRepository;
import com.bricks.productos_api.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;


    @Override
    public List<Categoria> listarCategorias(){

        return categoriaRepository.findAll();
    }
}
