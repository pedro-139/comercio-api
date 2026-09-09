package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.config.RestClientConfiguration;
import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.repository.CategoriaRepository;
import com.bricks.productos_api.service.CategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final RestClient restClient;


    @Override
    public List<Categoria> listarCategorias(){
        return restClient.get()
                .uri("https://api.escuelajs.co/api/v1/categories")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Categoria>>() {});

    }
}
