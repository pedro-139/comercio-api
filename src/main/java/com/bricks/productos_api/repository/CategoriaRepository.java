package com.bricks.productos_api.repository;

import com.bricks.productos_api.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/** Acceso a las categorías cacheadas en la base H2. */
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
}
