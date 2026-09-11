package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import com.bricks.productos_api.mapper.CategoriaMapper;
import com.bricks.productos_api.model.Categoria;
import com.bricks.productos_api.repository.CategoriaRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria c1;
    private Categoria c2;
    private CategoriaResponse resp1;
    private CategoriaResponse resp2;

    // Se ejecuta antes de cada test e inicializa los atributos comunes.
    @BeforeEach
    void setUp() {

        Categoria c1 = new Categoria();
        c1.setId(1L);
        c1.setName("Electrónica");
        this.c1 = c1;

        Categoria c2 = new Categoria();
        c2.setId(2L);
        c2.setName("Ropa");
        this.c2 = c2;

        CategoriaResponse resp1 = new CategoriaResponse();
        resp1.setId(1L);
        resp1.setName("Electrónica");
        this.resp1 = resp1;

        CategoriaResponse resp2 = new CategoriaResponse();
        resp2.setId(2L);
        resp2.setName("Ropa");
        this.resp2 = resp2;
    }


    // --------------------------
    // GET ALL
    // Debe devolver todas las categorías
    // --------------------------

    @Test
    void getAll() {

        // GIVEN
        when(categoriaRepository.findAll())
                .thenReturn(Arrays.asList(c1, c2));

        when(categoriaMapper.toDTO(c1))
                .thenReturn(resp1);

        when(categoriaMapper.toDTO(c2))
                .thenReturn(resp2);

        // WHEN
        List<CategoriaResponse> resultado =
                categoriaService.getAll();

        // THEN
        assertThat(resultado).isNotNull();
        assertThat(resultado).containsExactly(resp1, resp2);

        verify(categoriaRepository).findAll();
        verify(categoriaMapper).toDTO(c1);
        verify(categoriaMapper).toDTO(c2);
    }


    // --------------------------
    // FIND BY ID
    // Debe devolver la categoría
    // --------------------------

    @Test
    void findById() {

        Long id = 1L;

        // GIVEN
        when(categoriaRepository.findById(id))
                .thenReturn(Optional.of(c1));

        when(categoriaMapper.toDTO(c1))
                .thenReturn(resp1);

        // WHEN
        CategoriaResponse resultado =
                categoriaService.findById(id);

        // THEN
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getName()).isEqualTo(resp1.getName());

        verify(categoriaRepository).findById(id);
        verify(categoriaMapper).toDTO(c1);
    }

    // --------------------------
    // GET CATEGORIA BY ID
    // Debe devolver la categoría
    // --------------------------

    @Test
    void getCategoriaById() {

        Long id = 1L;

        // GIVEN
        when(categoriaRepository.findById(id))
                .thenReturn(Optional.of(c1));

        // WHEN
        Categoria resultado =
                categoriaService.getCategoriaById(id);

        // THEN
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getName()).isEqualTo(c1.getName());

        verify(categoriaRepository).findById(id);
    }
}