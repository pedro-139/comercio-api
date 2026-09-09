package com.bricks.productos_api.repository;

import com.bricks.productos_api.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {

        List<Producto> findByName(String name);

        List<Producto> findByPrice(Double price);

        List<Producto> findByStock(int stock);

        List<Producto> findByCategoria_Id(Long categoriaId);
}
