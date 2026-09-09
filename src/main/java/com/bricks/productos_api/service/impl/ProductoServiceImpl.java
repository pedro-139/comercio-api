package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.entity.Producto;
import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.repository.ProductoRepository;
import com.bricks.productos_api.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private ProductoRepository productoRepository; //No recomendado
    @Override
    public Producto registrarProducto(Producto producto){
        return productoRepository.save(producto);
    }

    @Override
    public List<Producto> listarProductos(){
        return  productoRepository.findAll();
    }

    @Override
    public Producto actualizarProducto(Long Id, Producto producto) throws Exception{
    Producto productoExistente = productoRepository.findById(Id)
        .orElseThrow(() -> new Exception("Producto con Id "+Id +" no encontrado"));

    //Si existe se actualiza
    productoExistente.setName(producto.getName());
    productoExistente.setCategory(producto.getCategory());
    productoExistente.setPrice(producto.getPrice());
    productoExistente.setStock(producto.getStock());

    return productoRepository.save(productoExistente);
    }

    @Override
    public void eliminarProducto(Long Id) throws Exception {
        Producto productoExistente = productoRepository.findById(Id)
                .orElseThrow(() -> new Exception("Producto con Id "+Id +" no encontrado"));

        // Si el producto a eliminar existe
        productoRepository.deleteById(Id);
    }

    @Override
    public Optional<Producto> buscarPorId(Long Id){
    return productoRepository.findById(Id);
    }
    @Override
    public List<Producto> obtenerProductosPorNombre(String nombre){
        return productoRepository.findByNombre(nombre);

    }

    @Override
    public List<Producto> obtenerProductosPorStock(int Stock){
        return productoRepository.findByStock(Stock);
    }

    @Override
    public List<Producto> obtenerProductosPorPrecio(Double precio){
        return productoRepository.findByPrecio(precio);
    }

    @Override
    public List<Producto> obtenerProductosPorCategoria(Categoria categoria){
        return productoRepository.findByCategory(categoria);
    }
}
