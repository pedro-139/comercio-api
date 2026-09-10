package com.bricks.productos_api.service.impl;


import com.bricks.productos_api.model.Categoria;
import com.bricks.productos_api.exception.ExternalServiceException;
import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.repository.CategoriaRepository;
import com.bricks.productos_api.service.CategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final RestClient restClient;


    @Override
    /*
    La proxima vez que se llame a este metodo no se ejecutara el codigo sino que devolvera
    lo guardado en "categoriasCache"
     */
    @Cacheable("categoriasCache")
    public List<Categoria> getAll() throws Exception {
        try{
            List<Categoria> categorias = categoriaRepository.findAll();

            if (!categorias.isEmpty())
                return categorias;

            sincronizarCategorias();
            return categoriaRepository.findAll();
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }

    }

    public Categoria findById(Long id) throws Exception {
        return categoriaRepository.findById(id)
                .orElseGet(() -> {
                    sincronizarCategorias();

                    return categoriaRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Categoria con ID " + id + " no encontrada"
                                    ));
                });
    }
    // Carga las categorias desde la API externa a la DB
    private void sincronizarCategorias() {
        try {
            List<Categoria> categoriasExternas = restClient
                    .get()
                    .uri("https://api.escuelajs.co/api/v1/categories")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Categoria>>() {});
            //verificar que no sea null
            categoriaRepository.saveAll(categoriasExternas);
        } catch (RestClientException exc) {
            throw new ExternalServiceException(
                    "No se pudo obtener el listado de categorias del servicio externo"
            );
        }
    }

}
