package com.bricks.productos_api.service.impl;


import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.entity.Producto;
import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.repository.ProductoRepository;
import com.bricks.productos_api.service.CategoriaService;
import com.bricks.productos_api.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;;

@Service

public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaService categoriaService;


    @Override
    public Producto registrarProducto(Long idCategoria, Producto producto){

        //Al momento de registrar un producto se valida si la categoria existe, si no existe
        // se maneja la excepción.
        Categoria categoria = categoriaService.buscarPorId(idCategoria);

        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }


    // Devuelve un String con el filtro de busqueda.
    private String determinarFiltroActivo(String name, Double price, Integer stock, Long idCategory) {
        if (name != null) return "name";
            else if (price != null) return "price";
                else if (stock != null) return "stock";
                    else if (idCategory != null) return "idCategoria";
        return "ninguno";
    }


    @Override
    // Solo acepta un filtro a la vez.
    public List<Producto> listarProductos(
            String name,
            Double price,
            Integer stock,
            Long categoryId
    ){
        String filtro = determinarFiltroActivo(name,price,stock, categoryId);
        return switch(filtro){
            case "name" -> productoRepository.findByName(name);
            case "price" -> productoRepository.findByPrice(price);
            case "stock" -> productoRepository.findByStock(stock);
            case "idCategoria" -> productoRepository.findByCategoria_Id(categoryId);
            default -> productoRepository.findAll();
        };
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
