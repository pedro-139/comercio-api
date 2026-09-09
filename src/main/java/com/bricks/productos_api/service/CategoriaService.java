/*package com.bricks.productos_api.service;

import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.exception.ExternalServiceException;
import com.bricks.productos_api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
/**
 * Consulta categorías al servicio externo, las persiste en H2 y las mantiene
 * en caché durante la vida de la aplicación.

public class CategoriaService {

        private final CategoriaRepository categoriaRepository;
        private final RestClient restClient;

        public CategoriaService(CategoriaRepository categoriaRepository,
                                @Value("${external-api.categories-url}") String categoriesUrl) {
            this.categoriaRepository = categoriaRepository;
            this.restClient = RestClient.builder().baseUrl(categoriesUrl).build();
        }

        @Cacheable("categories")
         La consulta externa se ejecuta una sola vez por ejecución de la API.
        public List<Categoria> obtenerCategorias(){
            List<Categoria> categories;
            try {
                categories = restClient.get()
                        .retrieve()
                        .body(new org.springframework.core.ParameterizedTypeReference<>() {});
            } catch (RestClientException exception) {
                throw new ExternalServiceException("No se pudieron obtener las categorías", exception);
            }
            if (categories == null || categories.isEmpty()) {
                throw new ExternalServiceException("La API externa no devolvió categorías");
            }
            return categoriaRepository.saveAll(categories);
        }
}
        */