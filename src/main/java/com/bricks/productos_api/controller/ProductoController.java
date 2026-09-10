package com.bricks.productos_api.controller;

import com.bricks.productos_api.model.Producto;
import com.bricks.productos_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( origins = "*") //Desde cualquier origen podemos acceder a los datos de la API
@RequestMapping("/api/products") // URI de los productos.
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /*
    El ResponseEntity<?> es una clase que representa todas las respuestas HTTP y '?' es un tipo de dato generico por lo que
    la respuestas puede ser de cualquier tipo.
     */


    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) Long idCategory) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productoService.findAll(name,price,stock,idCategory));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne (@PathVariable Long id){  // @PathVariable toma el id de la petición HTTP (GET /products/5) y lo convierte en un Long -> id = 5.
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productoService.findById(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Producto producto){ // @RequestBody toma el cuerpo de la petición HTTP y lo convierte en un objeto Producto.
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productoService.save(producto.getCategory().getId(),producto));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde\"}");
        }
    }

   @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Producto producto) {
       try {
           return ResponseEntity.status(HttpStatus.OK).body(productoService.update(id,producto));
       }catch(Exception e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde\"}");
       }
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(productoService.delete(id));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde\"}");
       }
   }
}
