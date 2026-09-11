package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.dto.categoria.CategoriaResponse;
import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.exception.ExternalServiceException;
import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.mapper.CategoriaMapper;
import com.bricks.productos_api.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

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

    private Categoria categoria1;
    private Categoria categoria2;

    private CategoriaResponse categoriaResponse1;
    private CategoriaResponse categoriaResponse2;

    @BeforeEach
    void setUp() {

        categoria1 = new Categoria();
        categoria1.setId(1L);
        categoria1.setName("Electrónica");

        categoria2 = new Categoria();
        categoria2.setId(2L);
        categoria2.setName("Ropa");

        categoriaResponse1 = new CategoriaResponse();
        categoriaResponse1.setId(1L);
        categoriaResponse1.setName("Electrónica");

        categoriaResponse2 = new CategoriaResponse();
        categoriaResponse2.setId(2L);
        categoriaResponse2.setName("Ropa");
    }


    // ==========================
    // GET ALL
    // ==========================

    @Test
    @DisplayName("getAll() debe devolver todas las categorías")
    void getAll_debeDevolverTodasLasCategorias() {

        when(categoriaRepository.findAll())
                .thenReturn(Arrays.asList(categoria1, categoria2));

        when(categoriaMapper.toDTO(categoria1))
                .thenReturn(categoriaResponse1);

        when(categoriaMapper.toDTO(categoria2))
                .thenReturn(categoriaResponse2);

        // Act
        List<CategoriaResponse> resultado =
                categoriaService.getAll();

        // Assert
        assertThat(resultado).hasSize(2);

        assertThat(resultado)
                .extracting(CategoriaResponse::getId)
                .containsExactly(1L, 2L);

        verify(categoriaRepository).findAll();

        verify(categoriaMapper).toDTO(categoria1);
        verify(categoriaMapper).toDTO(categoria2);
    }


    @Test
    @DisplayName("getAll() debe sincronizar las categorías si la base de datos está vacía")
    void getAll_debeSincronizarSiLaBaseEstaVacia() {

        // Primera búsqueda: BD vacía.
        when(categoriaRepository.findAll())
                .thenReturn(List.of())
                .thenReturn(Arrays.asList(categoria1, categoria2));

        // Act
        List<CategoriaResponse> resultado =
                categoriaService.getAll();

        // Assert
        assertThat(resultado).hasSize(2);

        verify(categoriaRepository, times(2)).findAll();

        verify(categoriaRepository)
                .saveAll(any());

        verify(categoriaMapper).toDTO(categoria1);
        verify(categoriaMapper).toDTO(categoria2);
    }


    // ==========================
    // FIND BY ID
    // ==========================

    @Test
    @DisplayName("findById() debe devolver la categoría cuando existe")
    void findById_debeDevolverCategoria() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria1));

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
    @DisplayName("findById() debe sincronizar si la categoría no existe localmente")
    void findById_debeSincronizarSiNoExiste() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(categoria1));

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
    @DisplayName("findById() debe lanzar excepción si la categoría no existe después de sincronizar")
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
    @DisplayName("getCategoriaById() debe devolver la categoría cuando existe")
    void getCategoriaById_debeDevolverCategoria() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria1));

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
    @DisplayName("getCategoriaById() debe lanzar excepción si no existe")
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