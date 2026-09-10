package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.dto.ProductoDTO;

import com.bricks.productos_api.mapper.ProductoMapper;
import com.bricks.productos_api.mapper.CategoriaMapper;
import com.bricks.productos_api.model.Categoria;
import com.bricks.productos_api.model.Producto;
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

    @Autowired
    private  CategoriaMapper categoriaMapper;

    @Override
    @Transactional //Significa que haran transacciones con la base de datos.

    public ProductoDTO save(ProductoDTO productoDTO) {

        Categoria categoria = categoriaMapper.toEntity(categoriaService.findById(productoDTO.getCategoryId()));


        Producto producto = productoMapper.toEntity(productoDTO);
        producto.setCategory(categoria);

        return productoMapper.toDTO(productoRepository.save(producto));
    }

    @Override
    //No uso @Transactional porque solo estoy leyendo.
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

    @Override
    @Transactional
    //Borra de la cache el producto modificado
    @CacheEvict( value = "productosCache", key = "#id")
    public ProductoDTO update(Long id, ProductoDTO productoDTO) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " +id +" no encontrado"));

        productoExistente.setName(productoDTO.getName());
        productoExistente.setPrice(productoDTO.getPrice());
        productoExistente.setStock(productoDTO.getStock());

        Categoria categoria = categoriaMapper.toEntity(
                categoriaService.findById(productoDTO.getCategoryId())
        );

        productoExistente.setCategory(categoria);

        //actualizo el producto
        Producto productoActualizado = productoRepository.save(productoExistente);
        return productoMapper.toDTO(productoActualizado);

    }

    @Override
    @Transactional
    //Borra de la cache el producto eliminado
    @CacheEvict( value = "productosCache", key = "#id")
    public boolean delete(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        } else{
            throw new ResourceNotFoundException("Producto con ID " +id +" no encontrado");
        }
    }


    @Override
    // Guarda el resultado en caché. La próxima vez, no ejecutará la consulta a la BD.
    @Cacheable(value = "productosCache", key = "#id")
    public ProductoDTO findById(Long id) {
        System.out.println("------> Accediento a base de datos <-----------");
        return productoRepository.findById(id)
                .map(productoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + id + " no encontrado"));
    }
}
