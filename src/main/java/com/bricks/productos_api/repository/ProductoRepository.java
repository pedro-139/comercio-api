package com.bricks.productos_api.repository;

import com.bricks.productos_api.entity.Categoria;
import com.bricks.productos_api.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {

    /*
     Optional representa el resultado de la búsqueda.
     El metodo busca un Producto según el criterio indicado
     y devuelve un Optional que contiene el Producto si existe,
     o un Optional vacío si no se encuentra ningún Producto.
     */



        List<Producto> findByName(String name);

        List<Producto> findByPrice(Double price);

        List<Producto> findByStock(int stock);

        List<Producto> findByCategoria(Categoria categoria);

         Optional<Producto> findById(Long id);
}
