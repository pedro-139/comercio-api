package com.bricks.productos_api.service;



import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.entity.Producto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


//Se recomienda implementar interfaces por
public interface ProductoService {

    Producto registrarProducto(Long idCategoria, Producto producto);

    List<Producto> listarProductos(
            String name,
            double price,
            int stock,
            Long idCategory
    );

    Producto actualizarProducto(Long id, Producto producto) throws Exception ;

    void eliminarProducto(Long id) throws Exception ;

    Producto buscarPorId(Long id);

}
