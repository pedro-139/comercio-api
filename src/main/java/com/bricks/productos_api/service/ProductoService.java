package com.bricks.productos_api.service;



import com.bricks.productos_api.dto.producto.ProductoRequest;
import com.bricks.productos_api.dto.producto.ProductoResponse;

import java.util.List;

public interface ProductoService {


    ProductoResponse create(ProductoRequest productoRequest);

    List<ProductoResponse> findAll(
            String name,
            Double price,
            Integer stock,
            Long categoryId
    );

    ProductoResponse update (Long id, ProductoRequest productoRequest);

    boolean delete(Long id);

    ProductoResponse findById(Long id);

}
