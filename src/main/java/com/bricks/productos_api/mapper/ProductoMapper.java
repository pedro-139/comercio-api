package com.bricks.productos_api.mapper;

import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import com.bricks.productos_api.dto.producto.ProductoRequest;
import com.bricks.productos_api.dto.producto.ProductoResponse;
import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProductoMapper {
    @Autowired
    private CategoriaMapper categoriaMapper;

    public ProductoResponse toDTO(Producto producto){
        if (producto == null) return null;

        ProductoResponse DTO = new ProductoResponse();
        DTO.setId(producto.getId());
        DTO.setName(producto.getName());
        DTO.setPrice(producto.getPrice());
        DTO.setStock(producto.getStock());

        Categoria categoria = producto.getCategory();

        CategoriaResponse categoriaResponse = categoriaMapper.toDTO(categoria);
        DTO.setCategory(categoriaResponse);

        return DTO;
    }

    public Producto toEntity(ProductoRequest productoRequest, Categoria categoria){
        if (productoRequest == null) return null;
        Producto producto = new Producto();
        producto.setName(productoRequest.getName());
        producto.setPrice(productoRequest.getPrice());
        producto.setStock(productoRequest.getStock());
        producto.setCategory(categoria);
        return producto;
    }


}
