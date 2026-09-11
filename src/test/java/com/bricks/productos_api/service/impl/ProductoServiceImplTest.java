package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.dto.producto.ProductoRequest;
import com.bricks.productos_api.dto.producto.ProductoResponse;
import com.bricks.productos_api.exception.BadRequestException;
import com.bricks.productos_api.model.Categoria;
import com.bricks.productos_api.model.Producto;
import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.mapper.ProductoMapper;
import com.bricks.productos_api.repository.ProductoRepository;
import com.bricks.productos_api.service.CategoriaService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Categoria categoria;
    private Producto p1;
    private Producto p2;
    private ProductoRequest req;
    private ProductoResponse resp1;
    private ProductoResponse resp2;

    // Se ejecuta antes de los test y inicializamos los atributos que utilizaremos en los testeos.
    @BeforeEach
    void setUp() {

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setName("Clothes");

        p1 = new Producto();
        p1.setId(1L);
        p1.setName("Camisa");
        p1.setPrice(850000.0);
        p1.setStock(10);
        p1.setCategory(categoria);

        p2 = new Producto();
        p2.setId(2L);
        p2.setName("Jean");
        p2.setPrice(25000.0);
        p2.setStock(20);
        p2.setCategory(categoria);

        req = new ProductoRequest();
        req.setName("Camisa nueva");
        req.setPrice(900000.0);
        req.setStock(15);
        req.setCategoryId(1L);

        resp1 = new ProductoResponse();
        resp1.setId(1L);
        resp1.setName("Camisa");
        resp1.setPrice(850000.0);
        resp1.setStock(10);

        resp2 = new ProductoResponse();
        resp2.setId(2L);
        resp2.setName("Jean");
        resp2.setPrice(25000.0);
        resp2.setStock(20);
    }

    // ==========================
    // CREATE
    // ==========================

    @Test

    void create_debeCrearProducto() {

        Producto producto = new Producto();
        producto.setName("Camisa nueva");
        producto.setPrice(900000.0);
        producto.setStock(15);
        producto.setCategory(categoria);

        Producto productoGuardado = new Producto();
        productoGuardado.setId(10L);
        productoGuardado.setName("Camisa nueva");
        productoGuardado.setPrice(900000.0);
        productoGuardado.setStock(15);
        productoGuardado.setCategory(categoria);

        ProductoResponse response = new ProductoResponse();
        response.setId(10L);
        response.setName("Camisa nueva");
        response.setPrice(900000.0);
        response.setStock(15);

        // La categoría existe.
        when(categoriaService.getCategoriaById(1L))
                .thenReturn(categoria);

        // El mapper convierte el request en entity.
        when(productoMapper.toEntity(req, categoria))
                .thenReturn(producto);

        // El repository guarda el producto.
        when(productoRepository.save(producto))
                .thenReturn(productoGuardado);

        // El mapper convierte la entity guardada en DTO.
        when(productoMapper.toDTO(productoGuardado))
                .thenReturn(response);

        // Act
        ProductoResponse resultado =
                productoService.create(req);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getName()).isEqualTo("Camisa nueva");

        // Verificamos el flujo completo.
        verify(categoriaService).getCategoriaById(1L);
        verify(productoMapper).toEntity(req, categoria);
        verify(productoRepository).save(producto);
        verify(productoMapper).toDTO(productoGuardado);
    }


    // ==========================
    // FIND ALL
    // ==========================

    @Test

    void findAll_debeDevolverTodosLosProductos() {

        when(productoRepository.findAll())
                .thenReturn(Arrays.asList(p1, p2));

        when(productoMapper.toDTO(p1))
                .thenReturn(resp1);

        when(productoMapper.toDTO(p2))
                .thenReturn(resp2);

        // Act
        List<ProductoResponse> resultado =
                productoService.findAll(null, null, null, null);

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado)
                .extracting(ProductoResponse::getId)
                .containsExactly(1L, 2L);

        verify(productoRepository).findAll();
        verify(productoMapper).toDTO(p1);
        verify(productoMapper).toDTO(p2);
    }


    @Test

    void findAll_debeFiltrarPorNombre() {

        when(productoRepository.findByName("Camisa"))
                .thenReturn(List.of(p1));

        when(productoMapper.toDTO(p1))
                .thenReturn(resp1);

        // Act
        List<ProductoResponse> resultado =
                productoService.findAll("Camisa", null, null, null);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getName())
                .isEqualTo("Camisa");

        verify(productoRepository).findByName("Camisa");
        verify(productoRepository, never()).findAll();
    }


    @Test

    void findAll_debeFiltrarPorPrecio() {

        when(productoRepository.findByPrice(850000.0))
                .thenReturn(List.of(p1));

        when(productoMapper.toDTO(p1))
                .thenReturn(resp1);

        // Act
        List<ProductoResponse> resultado =
                productoService.findAll(null, 850000.0, null, null);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPrice())
                .isEqualTo(850000.0);

        verify(productoRepository).findByPrice(850000.0);
        verify(productoRepository, never()).findAll();
    }


    @Test

    void findAll_debeFiltrarPorStock() {

        when(productoRepository.findByStock(10))
                .thenReturn(List.of(p1));

        when(productoMapper.toDTO(p1))
                .thenReturn(resp1);

        // Act
        List<ProductoResponse> resultado =
                productoService.findAll(null, null, 10, null);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getStock())
                .isEqualTo(10);

        verify(productoRepository).findByStock(10);
        verify(productoRepository, never()).findAll();
    }


    @Test

    void findAll_debeFiltrarPorCategoria() {

        when(productoRepository.findByCategoryId(1L))
                .thenReturn(List.of(p1, p2));

        when(productoMapper.toDTO(p1))
                .thenReturn(resp1);

        when(productoMapper.toDTO(p2))
                .thenReturn(resp2);

        // Act
        List<ProductoResponse> resultado =
                productoService.findAll(null, null, null, 1L);

        // Assert
        assertThat(resultado).hasSize(2);

        verify(productoRepository).findByCategoryId(1L);
        verify(productoRepository, never()).findAll();
    }


    @Test

    void findAll_debeLanzarExcepcionSiHayMasDeUnFiltro() {
        Assertions.assertThatThrownBy(() -> productoService.findAll("Camisa", null, null, 1L)).isInstanceOf(BadRequestException.class);
        verify(productoRepository, never()).findByName(any());
        verify(productoRepository, never()).findByCategoryId(any());
        verify(productoRepository, never()).findAll();
}


    // ==========================
    // FIND BY ID
    // ==========================

    @Test

    void findById_debeDevolverProducto() {

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(p1));

        when(productoMapper.toDTO(p1))
                .thenReturn(resp1);

        // Act
        ProductoResponse resultado =
                productoService.findById(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getName()).isEqualTo("Camisa");

        verify(productoRepository).findById(1L);
        verify(productoMapper).toDTO(p1);
    }


    @Test

    void findById_debeLanzarExcepcionSiNoExiste() {

        when(productoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productoRepository).findById(99L);
        verify(productoMapper, never()).toDTO(any());
    }


    // ==========================
    // UPDATE
    // ==========================

    @Test

    void update_debeActualizarProducto() {

        Producto existente = new Producto();
        existente.setId(1L);
        existente.setName("Camisa");
        existente.setPrice(850000.0);
        existente.setStock(10);
        existente.setCategory(categoria);

        ProductoResponse response = new ProductoResponse();
        response.setId(1L);
        response.setName("Camisa nueva");
        response.setPrice(900000.0);
        response.setStock(15);

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(categoriaService.getCategoriaById(1L))
                .thenReturn(categoria);

        when(productoRepository.save(existente))
                .thenReturn(existente);

        when(productoMapper.toDTO(existente))
                .thenReturn(response);

        // Act
        ProductoResponse resultado =
                productoService.update(1L, req);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getName())
                .isEqualTo("Camisa nueva");

        assertThat(existente.getName())
                .isEqualTo("Camisa nueva");

        assertThat(existente.getPrice())
                .isEqualTo(900000.0);

        assertThat(existente.getStock())
                .isEqualTo(15);

        verify(productoRepository).findById(1L);
        verify(categoriaService).getCategoriaById(1L);
        verify(productoRepository).save(existente);
        verify(productoMapper).toDTO(existente);
    }


    @Test

    void update_debeLanzarExcepcionSiNoExiste() {

        when(productoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                productoService.update(99L, req)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(productoRepository).findById(99L);
        verify(categoriaService, never()).getCategoriaById(any());
        verify(productoRepository, never()).save(any());
    }


    // ==========================
    // DELETE
    // ==========================

    @Test

    void delete_debeEliminarProducto() {

        when(productoRepository.existsById(1L))
                .thenReturn(true);

        // Act
        productoService.delete(1L);

        // Assert
        verify(productoRepository)
                .deleteById(1L);
    }


    @Test

    void delete_debeLanzarExcepcionSiNoExiste() {

        when(productoRepository.existsById(99L))
                .thenReturn(false);

        assertThatThrownBy(() ->
                productoService.delete(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(productoRepository, never())
                .deleteById(99L);
    }
}