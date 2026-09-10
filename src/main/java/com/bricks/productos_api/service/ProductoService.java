package com.bricks.productos_api.service;



import com.bricks.productos_api.dto.ProductoDTO;
import java.util.List;

public interface ProductoService {

    ProductoDTO save(ProductoDTO productoDTO);

    List<ProductoDTO> findAll(
            String name,
            Double price,
            Integer stock,
            Long categoryId
    );

    ProductoDTO update (Long id, ProductoDTO productoDTO);

    boolean delete(Long id);

    ProductoDTO findById(Long id);

}
