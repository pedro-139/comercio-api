package com.bricks.productos_api.service;


import com.bricks.productos_api.entity.Categoria;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
// accept?
//@HttpExchange( url = "https://api.escuelajs.co/api/v1/categories", accept = "application/json")
public interface CategoriaService {

   // @GetExchange
  //  List<Categoria> getAllCategorias();

    List<Categoria> listarCategorias();

    Categoria buscarPorId(Long id);

}