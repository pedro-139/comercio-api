package com.bricks.productos_api.service;



import com.bricks.productos_api.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {

    Producto save(Long idCategoria, Producto producto) throws Exception;

    List<Producto> findAll(
            String name,
            Double price,
            Integer stock,
            Long categoryId
    ) throws Exception;

    Producto update (Long id, Producto producto)throws Exception ;

    boolean delete(Long id) throws Exception;

    Producto findById(Long id)throws Exception;

}
