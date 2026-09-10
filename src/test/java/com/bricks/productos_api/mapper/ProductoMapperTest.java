package com.bricks.productos_api.mapper;

import com.bricks.productos_api.config.ModelMapperConfig;
import com.bricks.productos_api.dto.ProductoDTO;
import com.bricks.productos_api.model.Categoria;
import com.bricks.productos_api.model.Producto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Verifica el contrato entidad/DTO, especialmente la conversión de category.id a categoryId. */
class ProductoMapperTest {

    private final ProductoMapper mapper = new ProductoMapper();

    ProductoMapperTest() {
        try {
            var field = ProductoMapper.class.getDeclaredField("modelMapper");
            field.setAccessible(true);
            field.set(mapper, new ModelMapperConfig().modelMapper());
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }

    @Test
    void toDTO_debeExponerElIdDeLaCategoria() {
        Producto producto = new Producto(4L, "Teclado", 100.0, 2,
                new Categoria(9L, "Electrónica"));

        ProductoDTO resultado = mapper.toDTO(producto);

        assertEquals(4L, resultado.getId());
        assertEquals(9L, resultado.getCategoryId());
    }
}
