package com.bricks.productos_api.service;



import com.bricks.productos_api.entity.Producto;
import java.util.List;

public interface ProductoService {

    Producto registrarProducto(Long idCategoria, Producto producto);

    List<Producto> listarProductos(
            String name,
            Double price,
            Integer stock,
            Long categoryId
    );

    Producto actualizarProducto(Long id, Producto producto) ;

    void eliminarProducto(Long id) ;

    Producto buscarPorId(Long id);

}
