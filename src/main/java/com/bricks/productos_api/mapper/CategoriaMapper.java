package com.bricks.productos_api.mapper;

import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import com.bricks.productos_api.entity.Categoria;
import org.springframework.stereotype.Component;


@Component
public class CategoriaMapper {


    public CategoriaResponse toDTO(Categoria categoria) {
        if (categoria == null) return null;
        return  new CategoriaResponse(categoria.getId(),categoria.getName());
    }

    public Categoria toEntity(CategoriaResponse DTO){
        if (DTO == null) return null;
        return new Categoria(DTO.getId(),DTO.getName());
    }

}
