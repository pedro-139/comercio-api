package com.bricks.productos_api.mapper;

import com.bricks.productos_api.dto.ProductoDTO;
import com.bricks.productos_api.model.Producto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    @Autowired
    private ModelMapper modelMapper;

    public Producto toEntity(ProductoDTO productoDTO){
        Producto producto = modelMapper.map(productoDTO, Producto.class);
        producto.setCategory(null);
        return producto;
    }

    public void toEntity(ProductoDTO productoDTO, Producto productoExistente){
        modelMapper.map(productoDTO,productoExistente);
    }

    public ProductoDTO toDTO(Producto producto){
        ProductoDTO productoDTO = modelMapper.map(producto, ProductoDTO.class);
        if (producto.getCategory() != null) {
            productoDTO.setCategoryId(producto.getCategory().getId());
        }
        return productoDTO;
    }
}
