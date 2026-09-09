package com.bricks.productos_api.service.impl;


import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.exception.ExternalServiceException;
import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.repository.CategoriaRepository;
import com.bricks.productos_api.service.CategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final RestClient restClient;


    @Override
    public List<Categoria> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();

        if (!categorias.isEmpty()) {
            return categorias;
        }

        sincronizarCategorias();
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id) //Devuelve la categoria por su id
                .orElseGet(() -> {              // si no hay categorias en la DB
                    sincronizarCategorias();    //sincroniza

                    return categoriaRepository.findById(id) //Luego de sincronizar vuelve a buscar la categoria por su id
                            .orElseThrow(() ->              //Si no la encuentra devuelve Not Found.
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
