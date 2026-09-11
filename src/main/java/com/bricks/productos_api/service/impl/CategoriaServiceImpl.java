package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import com.bricks.productos_api.model.Categoria;
import com.bricks.productos_api.exception.ExternalServiceException;
import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.mapper.CategoriaMapper;
import com.bricks.productos_api.repository.CategoriaRepository;
import com.bricks.productos_api.service.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private static final String CATEGORIES_URL = "https://api.escuelajs.co/api/v1/categories";

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private RestClient restClient;

    @Autowired
    private CategoriaMapper categoriaMapper;



    @Override
    public List<CategoriaResponse> getAll() {
        List<Categoria> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) {
            sincronizarCategorias();
            categorias = categoriaRepository.findAll();
        }
        return categorias.stream().map(categoriaMapper::toDTO).toList();
    }

    @Override
    public CategoriaResponse findById(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseGet(() -> {
                    sincronizarCategorias();

                    return categoriaRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Categoría con ID " + id + " no encontrada"));
                });
        return categoriaMapper.toDTO(categoria);
    }

    @Override
    public Categoria getCategoriaById(Long id){
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con ID " + id + " no encontrada"));
    }

    private void sincronizarCategorias() {
        try {
            List<Categoria> categoriasExternas = restClient
                    .get()
                    .uri(CATEGORIES_URL)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Categoria>>() {});
            if (categoriasExternas == null) {
                throw new ExternalServiceException("El servicio externo no devolvió categorías");
            }
            categoriaRepository.saveAll(categoriasExternas);
        } catch (RestClientException e) {
            throw new ExternalServiceException("No se pudo obtener el listado de categorias del servicio externo"
            );
        }
    }

}
