package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.dto.producto.ProductoRequest;
import com.bricks.productos_api.dto.producto.ProductoResponse;

import com.bricks.productos_api.model.Categoria;
import com.bricks.productos_api.model.Producto;
import com.bricks.productos_api.mapper.ProductoMapper;
import com.bricks.productos_api.repository.ProductoRepository;
import com.bricks.productos_api.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaServiceImpl categoriaService;

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

    // Se ejecuta antes de cada test e inicializa los atributos comunes que usan varios testeos.
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

    // --------------------------
    // CREATE
    // --------------------------

    @DisplayName("Dado un producto que queremos crear, cuando llamemos a 'create' esperamos que el producto este creado")
    @Test
    void create() {
        // GIVEN

        // El objeto que el mapper arma a partir del request.
        Producto producto = new Producto();
        producto.setName("Camisa nueva");
        producto.setPrice(900000.0);
        producto.setStock(15);
        producto.setCategory(categoria);

        // El objeto que "vuelve" del repository ya persistido (con ID).
        Producto guardado = new Producto();
        guardado.setId(10L);
        guardado.setName("Camisa nueva");
        guardado.setPrice(900000.0);
        guardado.setStock(15);
        guardado.setCategory(categoria);

        // El DTO de respuesta que se le muestra al cliente.
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
                .thenReturn(guardado);

        // El mapper convierte la entity guardada en el DTO de respuesta
        when(productoMapper.toDTO(guardado))
                .thenReturn(response);

        // WHEN (Producto respuesta)
        ProductoResponse resultado =
                productoService.create(req);

        // THEN (Comprobamos si respuesta es igual a esperado).
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(response.getId());
        assertThat(resultado.getName()).isEqualTo(response.getName());
        assertThat(resultado.getPrice()).isEqualTo(response.getPrice());
        assertThat(resultado.getStock()).isEqualTo(response.getStock());
        assertThat(resultado.getCategory()).isEqualTo(response.getCategory());


        // Verificamos que el service haya llamado a cada dependencia en el flujo Producto.
        verify(categoriaService).getCategoriaById(1L);
        verify(productoMapper).toEntity(req, categoria);
        verify(productoRepository).save(producto);
        verify(productoMapper).toDTO(guardado);
    }


    // -------------------------
    // FIND ALL
    // Debe devolver todos los productos y filtrar por nombre, precio, stock y categoría.
    // -------------------------

    @DisplayName("Dado que no hay filtros, cuando llamemos a 'findAll' esperamos que devuelva todos los productos")
    @Test
    void findAll() {
        // GIVEN
        when(productoRepository.findAll())
                .thenReturn(Arrays.asList(p1, p2)); // Devuelve una lista con los Productos p1 y p2

        when(productoMapper.toDTO(p1))
                .thenReturn(resp1); //Convierte la entity p1 en Response resp1

        when(productoMapper.toDTO(p2))
                .thenReturn(resp2); // Convierte la entity p2 en Response resp2

        // WHEN llama al metodo sin filtros
        List<ProductoResponse> resultado =
                productoService.findAll(null, null, null, null);

        // THEN Comprobamos que resultado es igual a esperado
        assertThat(resultado).containsExactly(resp1,resp2);

        //Verificamos que se hayan llamado a los metodos.
        verify(productoRepository).findAll();
        verify(productoMapper).toDTO(p1);
        verify(productoMapper).toDTO(p2);
    }


    @DisplayName("Dado un filtro por nombre, cuando llamemos a 'findAll' esperamos que devuelva solo los productos que matchean")
    @Test
    void filtrarPorNombre() {
        String filtro = "Camisa";
        // GIVEN
        when(productoRepository.findByName(filtro))//Devuelva lista con filtro 'Camisa'
                .thenReturn(List.of(p1));

        when(productoMapper.toDTO(p1))  // Convierte la entity p1 en Response resp1
                .thenReturn(resp1);

        // WHEN llama al metodo con el filtro name
        List<ProductoResponse> resultado =
                productoService.findAll(filtro, null, null, null);

        // THEN
        assertThat(resultado).containsExactly(resp1);

        verify(productoRepository).findByName(filtro);
    }


    @DisplayName("Dado un filtro por precio, cuando llamemos a 'findAll' esperamos que devuelva solo los productos que matchean")
    @Test
    void filtrarPorPrecio() {
        Double filtro = 850000.0;
        // GIVEN
        when(productoRepository.findByPrice(filtro))
                .thenReturn(List.of(p1));

        when(productoMapper.toDTO(p1))
                .thenReturn(resp1);

        // WHEN
        List<ProductoResponse> resultado =
                productoService.findAll(null, filtro, null, null);

        // THEN
        assertThat(resultado).containsExactly(resp1);

        verify(productoRepository).findByPrice(filtro);

    }


    @DisplayName("Dado un filtro por stock, cuando llamemos a 'findAll' esperamos que devuelva solo los productos que matchean")
    @Test
    void filtrarPorStock() {
        Integer filtro = 10;
        // GIVEN
        when(productoRepository.findByStock(filtro))
                .thenReturn(List.of(p1));

        when(productoMapper.toDTO(p1))
                .thenReturn(resp1);

        // WHEN
        List<ProductoResponse> resultado =
                productoService.findAll(null, null, filtro, null);

        // THEN
        assertThat(resultado).containsExactly(resp1);

        verify(productoRepository).findByStock(filtro);

    }


    @DisplayName("Dado un filtro por categoría, cuando llamemos a 'findAll' esperamos que devuelva solo los productos que matchean")
    @Test
    void filtrarPorCategoria() {
        Long filtro = 1L;
        // GIVEN
        when(productoRepository.findByCategoryId(filtro))
                .thenReturn(List.of(p1, p2));

        when(productoMapper.toDTO(p1))
                .thenReturn(resp1);

        when(productoMapper.toDTO(p2))
                .thenReturn(resp2);

        // WHEN
        List<ProductoResponse> resultado =
                productoService.findAll(null, null, null, filtro);

        // THEN
        assertThat(resultado).containsExactly(resp1,resp2);

        verify(productoRepository).findByCategoryId(filtro);

    }



    // -------------------------
    // FIND BY ID
    // Debe devolver el producto
    // Debe lanzar exception
    //--------------------------

    @DisplayName("Dado un producto existente, cuando llamemos a 'findById' esperamos que lo devuelva")
    @Test
    void findById() {

        Long id = 1L;

        // GIVEN
        when(productoRepository.findById(id))       //Buscamos el Optional del producto p1 mediante su id.
                .thenReturn(Optional.of(p1));

        when(productoMapper.toDTO(p1))  //Convertimos la entidad p1 a Response resp1.
                .thenReturn(resp1);

        // WHEN
        ProductoResponse resultado =
                productoService.findById(id);

        // THEN
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getName()).isEqualTo(resp1.getName());
        assertThat(resultado.getPrice()).isEqualTo(resp1.getPrice());
        assertThat(resultado.getStock()).isEqualTo(resp1.getStock());
        assertThat(resultado.getCategory()).isEqualTo(resp1.getCategory());

        verify(productoRepository).findById(id);
        verify(productoMapper).toDTO(p1);
    }


    // -------------------------
    // UPDATE
    // Debe actualizar el producto
    // -------------------------

    @DisplayName("Dado un producto existente, cuando llamemos a 'update' esperamos que sus datos se actualicen")
    @Test
    void update() {
        Long idUpdate = 1L;

        // GIVEN
        Producto existente = new Producto();
        existente.setId(idUpdate);
        existente.setName("Camisa");
        existente.setPrice(850000.0);
        existente.setStock(10);
        existente.setCategory(categoria);

        ProductoResponse response = new ProductoResponse();
        response.setId(idUpdate);
        response.setName("Camisa nueva");
        response.setPrice(900000.0);
        response.setStock(15);

        when(productoRepository.findById(idUpdate))
                .thenReturn(Optional.of(existente));

        when(categoriaService.getCategoriaById(req.getCategoryId()))
                .thenReturn(categoria);

        when(productoRepository.save(existente))
                .thenReturn(existente);

        when(productoMapper.toDTO(existente))
                .thenReturn(response);

        // WHEN
        ProductoResponse resultado =
                productoService.update(idUpdate, req);

        // THEN

        // Comprobamos que el resultado devuelto por update sea correcto
        assertThat(resultado).isNotNull();

        assertThat(resultado.getName())
                .isEqualTo(response.getName());


        //Comprobamos que la entidad existente haya sido modificada correctamente.
        assertThat(existente.getName())
                .isEqualTo(response.getName());

        assertThat(existente.getPrice())
                .isEqualTo(response.getPrice());

        assertThat(existente.getStock())
                .isEqualTo(response.getStock());

        verify(productoRepository).findById(idUpdate);
        verify(categoriaService).getCategoriaById(req.getCategoryId());
        verify(productoRepository).save(existente);
        verify(productoMapper).toDTO(existente);
    }


    // --------------------------
    // DELETE
    // Debe eliminar producto
    // --------------------------

    @DisplayName("Dado un producto existente, cuando llamemos a 'delete' esperamos que se elimine")
    @Test
    void delete() {
        // GIVEN
        when(productoRepository.existsById(1L))
                .thenReturn(true);

        // WHEN
        productoService.delete(1L);

        // THEN
        verify(productoRepository)
                .deleteById(1L);
    }

}