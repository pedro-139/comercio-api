package com.bricks.productos_api.service;



import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.entity.Producto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


//Se recomienda implementar interfaces por
public interface ProductoService {

    Producto registrarProducto(Producto producto);

    List<Producto> listarProductos();

    Producto actualizarProducto(Long Id, Producto producto) throws Exception ;

    void eliminarProducto(Long Id) throws Exception ;

    List<Producto> obtenerProductosPorNombre(String nombre);

    List<Producto> obtenerProductosPorStock(int Stock);

    List<Producto> obtenerProductosPorPrecio(Double precio);

    List<Producto> obtenerProductosPorCategoria(Categoria categoria);

    Optional<Producto> buscarPorId(Long Id);

}
