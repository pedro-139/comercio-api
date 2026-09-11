package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import com.bricks.productos_api.model.Categoria;
import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.mapper.CategoriaMapper;
import com.bricks.productos_api.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria c1;
    private Categoria c2;

    private CategoriaResponse resp1;
    private CategoriaResponse resp2;

    @BeforeEach
    void setUp() {

        c1 = new Categoria();
        c1.setId(1L);
        c1.setName("Electrónica");

        c2 = new Categoria();
        c2.setId(2L);
        c2.setName("Ropa");

        resp1 = new CategoriaResponse();
        resp1.setId(1L);
        resp1.setName("Electrónica");

        resp2 = new CategoriaResponse();
        resp2.setId(2L);
        resp2.setName("Ropa");
    }


    // ==========================
    // GET ALL
    // ==========================

    @Test
    void getAll_debeDevolverTodasLasCategorias() {

        when(categoriaRepository.findAll())
                .thenReturn(Arrays.asList(c1, c2));

        when(categoriaMapper.toDTO(c1))
                .thenReturn(resp1);

        when(categoriaMapper.toDTO(c2))
                .thenReturn(resp2);

        // Act
        List<CategoriaResponse> resultado =
                categoriaService.getAll();

        // Assert
        assertThat(resultado).hasSize(2);

        assertThat(resultado)
                .extracting(CategoriaResponse::getId)
                .containsExactly(1L, 2L);

        verify(categoriaRepository).findAll();

        verify(categoriaMapper).toDTO(c1);
        verify(categoriaMapper).toDTO(c2);
    }


    @Test
    void getAll_debeSincronizarSiLaBaseEstaVacia() {

        // Primera búsqueda: BD vacía.
        when(categoriaRepository.findAll())
                .thenReturn(List.of())
                .thenReturn(Arrays.asList(c1, c2));

        // Act
        List<CategoriaResponse> resultado =
                categoriaService.getAll();

        // Assert
        assertThat(resultado).hasSize(2);

        verify(categoriaRepository, times(2)).findAll();

        verify(categoriaRepository)
                .saveAll(any());

        verify(categoriaMapper).toDTO(c1);
        verify(categoriaMapper).toDTO(c2);
    }


    // ==========================
    // FIND BY ID
    // ==========================

    @Test
    void findById_debeDevolverCategoria() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(c1));

        // Act
        CategoriaResponse resultado =
                categoriaService.findById(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getName())
                .isEqualTo("Electrónica");

        verify(categoriaRepository)
                .findById(1L);
    }


    @Test
    void findById_debeSincronizarSiNoExiste() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(c1));

        // Act
        CategoriaResponse resultado =
                categoriaService.findById(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);

        verify(categoriaRepository, times(2))
                .findById(1L);

        verify(categoriaRepository)
                .saveAll(any());
    }


    @Test
    void findById_debeLanzarExcepcionSiNoExiste() {

        when(categoriaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                categoriaService.findById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(categoriaRepository, times(2))
                .findById(99L);

        verify(categoriaRepository)
                .saveAll(any());
    }


    // ==========================
    // GET CATEGORIA BY ID
    // ==========================

    @Test
    void getCategoriaById_debeDevolverCategoria() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(c1));

        // Act
        Categoria resultado =
                categoriaService.getCategoriaById(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getName())
                .isEqualTo("Electrónica");

        verify(categoriaRepository)
                .findById(1L);
    }


    @Test
    void getCategoriaById_debeLanzarExcepcionSiNoExiste() {

        when(categoriaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                categoriaService.getCategoriaById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(categoriaRepository)
                .findById(99L);

        verify(categoriaRepository, never())
                .saveAll(any());
    }
}