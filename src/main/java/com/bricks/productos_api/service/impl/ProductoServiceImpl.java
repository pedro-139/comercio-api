package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.dto.producto.ProductoRequest;
import com.bricks.productos_api.dto.producto.ProductoResponse;
import com.bricks.productos_api.mapper.ProductoMapper;
import com.bricks.productos_api.model.Categoria;
import com.bricks.productos_api.model.Producto;
import com.bricks.productos_api.exception.ResourceNotFoundException;

import com.bricks.productos_api.repository.ProductoRepository;
import com.bricks.productos_api.service.CategoriaService;
import com.bricks.productos_api.service.ProductoService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;


@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoMapper productoMapper;


    @Override
    public ProductoResponse create(ProductoRequest productoRequest) {

        Categoria categoria = categoriaService.getCategoriaById(productoRequest.getCategoryId());

        Producto producto = productoMapper.toEntity(productoRequest,categoria);
        Producto guardado = productoRepository.save(producto);

        return productoMapper.toDTO(guardado);
    }



    @Override
    // Solo acepta un filtro a la vez.
    public List<ProductoResponse> findAll(String name, Double price, Integer stock, Long categoryId) {
        List<Producto> productos;
        if (name != null) {
            productos = productoRepository.findByName(name);
        } else if (price != null) {
            productos = productoRepository.findByPrice(price);
        } else if (stock != null) {
            productos = productoRepository.findByStock(stock);
        } else if (categoryId != null) {
            productos = productoRepository.findByCategoryId(categoryId);
        } else {
            productos = productoRepository.findAll();
        }
        return productos.stream().map(productoMapper::toDTO).toList();
    }


    @Override
    @CacheEvict( value = "productosCache", key = "#id") // Borra de la cache el producto modificado
    public ProductoResponse update(Long id, ProductoRequest productoRequest) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " +id +" no encontrado"));

        existente.setName(productoRequest.getName());
        existente.setPrice(productoRequest.getPrice());
        existente.setStock(productoRequest.getStock());

        Categoria categoria = categoriaService.getCategoriaById(productoRequest.getCategoryId());
        existente.setCategory(categoria);

        //actualizo el producto
        Producto productoActualizado = productoRepository.save(existente);
        return productoMapper.toDTO(productoActualizado);

    }

    @Override
    @CacheEvict( value = "productosCache", key = "#id")// Borra de la cache el producto eliminado
    public void delete(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        } else{
            throw new ResourceNotFoundException("Producto con ID " +id +" no encontrado");
        }
    }



    @Override
    @Cacheable(value = "productosCache", key = "#id") // Guarda el resultado en caché. La próxima vez, no ejecutará la consulta a la BD.
    public ProductoResponse findById(Long id) {
        System.out.println("------> Accediento a base de datos <-----------");
        Producto producto =  productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + id + " no encontrado"));
        return productoMapper.toDTO(producto);
    }
}
