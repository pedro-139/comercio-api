package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.entity.Producto;
import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.repository.CategoriaRepository;
import com.bricks.productos_api.repository.ProductoRepository;
import com.bricks.productos_api.service.ProductoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;

@Service

public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private ProductoRepository productoRepository; //No recomendado

    @Autowired
    private CategoriaRepository categoriaRepository;


    @Override
    public Producto registrarProducto(Long idCategoria, Producto producto){
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria con ID " +idCategoria +" no encontrada"));

        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }

    @Override
    public List<Producto> listarProductos(
            String name,
            double price,
            int stock,
            Long idCategory
    ){
        return  productoRepository.findAll();
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
    Producto productoExistente = productoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Producto con Id "+id +" no encontrado"));

    //Si existe se actualiza
    productoExistente.setName(producto.getName());
    productoExistente.setCategoria(producto.getCategoria());
    productoExistente.setPrice(producto.getPrice());
    productoExistente.setStock(producto.getStock());

    return productoRepository.save(productoExistente);
    }

    @Override
    public void eliminarProducto(Long id)  {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con Id "+id +" no encontrado"));

        // Si el producto a eliminar existe
        productoRepository.deleteById(id);
    }

    @Override
    public Producto buscarPorId(Long id){
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " +id +" no encontrado"));
    }
}
