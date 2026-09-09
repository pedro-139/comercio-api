package com.bricks.productos_api.controller;

import com.bricks.productos_api.entity.Producto;
import com.bricks.productos_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products") //Segun el video se le antepone api por buena practica, /api/products
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    /*
    El ResponseEntity<?> es una clase que representa todas las respuestas HTTP y '?' es un tipo de dato generico por lo que
    la respuestas puede ser de cualquier tipo.
     */
    @PostMapping
    public ResponseEntity<?> registrarProducto(@RequestBody Producto producto){ // @RequestBody toma el cuerpo de la petición HTTP y lo convierte en un objeto Producto.
        Producto nuevoProducto = productoService.registrarProducto(producto.getCategoria().getId(),producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @GetMapping

    public ResponseEntity<List<Producto>> listarProductos(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) Long idCategory) {
        List<Producto> productos = productoService.listarProductos(name,price,stock,idCategory);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId (@PathVariable Long id){  // @PathVariable toma el id de la petición HTTP (GET /products/5) y lo convierte en un Long -> id = 5.
        Producto producto = productoService.buscarPorId(id);
        return ResponseEntity.ok(producto);
    }

   @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
       try {
           Producto productoActualizado = new Producto();
           productoActualizado.setId(id);
           productoActualizado.setName(producto.getName());
           productoActualizado.setPrice(producto.getPrice());
           productoActualizado.setStock(producto.getStock());
           productoActualizado.setCategoria(producto.getCategoria());

           Producto productoDB = productoService.actualizarProducto(id,productoActualizado);
           return ResponseEntity.ok(productoActualizado);
       }
       catch(Exception exc){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exc.getMessage());
       }

   }

   @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id){
        try{
            productoService.eliminarProducto(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }
        catch(Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
       }
   }
}
