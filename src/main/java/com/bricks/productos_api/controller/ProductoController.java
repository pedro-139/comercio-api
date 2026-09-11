package com.bricks.productos_api.controller;

import com.bricks.productos_api.dto.producto.ProductoRequest;
import com.bricks.productos_api.dto.producto.ProductoResponse;
import com.bricks.productos_api.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController (ProductoService productoService){
        this.productoService = productoService;
    }

    // ------------------------------------------
    // GET /products
    // ------------------------------------------
    @Operation(
            summary = "Listado de productos",
            description = "Devuelve todos los productos."
    )
    @ApiResponse(
            responseCode = "200",
            description = "listado devuelto exitosamente"
    )
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(productoService.findAll(name, price, stock, categoryId));
    }

    // ------------------------------------------
    // GET /products/{id}
    // ------------------------------------------
    @Operation(
            summary = "Obtener producto por id",
            description = "Devuelve un producto existente identificado por su ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"


            )
})
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    // ------------------------------------------
    // POST /products
    // ------------------------------------------

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"


            )
    })
    @PostMapping
    public ResponseEntity<ProductoResponse> save(@Valid @RequestBody ProductoRequest productoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.create(productoRequest));
    }

    // ------------------------------------------
    // PUT /products/{id}
    // ------------------------------------------
    @PutMapping("/{id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"

            )
    })
    public ResponseEntity<ProductoResponse> update(@PathVariable Long id, @Valid @RequestBody ProductoRequest productoRequest) {
        return ResponseEntity.ok(productoService.update(id, productoRequest));
    }

    // ------------------------------------------
    // DELETE /products/{id}
    // ------------------------------------------

    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"

            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
