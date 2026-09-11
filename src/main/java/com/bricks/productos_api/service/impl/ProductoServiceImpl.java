package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import com.bricks.productos_api.dto.producto.ProductoRequest;
import com.bricks.productos_api.dto.producto.ProductoResponse;
import com.bricks.productos_api.mapper.ProductoMapper;
import com.bricks.productos_api.mapper.CategoriaMapper;
import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.entity.Producto;
import com.bricks.productos_api.exception.ResourceNotFoundException;

import com.bricks.productos_api.repository.ProductoRepository;
import com.bricks.productos_api.service.CategoriaService;
import com.bricks.productos_api.service.ProductoService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;


@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private  ProductoRepository productoRepository;

    @Autowired
    private  CategoriaService categoriaService;

    @Autowired
    private  ProductoMapper productoMapper;


    @Override
    @Transactional
    @CacheEvict( value = "productosCache", key = "'all'") // Borra la caché
    public ProductoResponse create(ProductoRequest productoRequest) {

        Categoria categoria = categoriaService.getCategoriaById(productoRequest.getCategoryId());

        Producto producto = productoMapper.toEntity(productoRequest,categoria);
        Producto guardado = productoRepository.save(producto);

        return productoMapper.toDTO(guardado);
    }



   // @Override
   /*
    // Solo acepta un filtro a la vez.
    public List<ProductoDTO> findAll(String name, Double price, Integer stock, Long categoryId) {
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

*/

    @Override
    @Transactional
    @CacheEvict( value = "productosCache", key = "#id") //Borra de la cache el producto modificado
    public ProductoResponse update(Long id, ProductoRequest productoRequest) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " +id +" no encontrado"));

        productoExistente.setName(productoRequest.getName());
        productoExistente.setPrice(productoRequest.getPrice());
        productoExistente.setStock(productoRequest.getStock());

        Categoria category = categoriaService.getCategoriaById(productoRequest.getCategoryId());
        productoExistente.setCategory(category);

        //actualizo el producto
        Producto productoActualizado = productoRepository.save(productoExistente);
        return productoMapper.toDTO(productoActualizado);

    }

    @Override
    @Transactional
    @CacheEvict( value = "productosCache", key = "#id")//Borra de la cache el producto eliminado
    public boolean delete(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
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
