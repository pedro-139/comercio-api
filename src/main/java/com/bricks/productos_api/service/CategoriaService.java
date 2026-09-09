package com.bricks.productos_api.service;

import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.exception.ExternalServiceException;
import com.bricks.productos_api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;


public interface CategoriaService {
    List<Categoria> listarCategorias();

}