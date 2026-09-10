package com.bricks.productos_api.service.impl;

import com.bricks.productos_api.model.Categoria;
import com.bricks.productos_api.model.Producto;
import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.repository.ProductoRepository;
import com.bricks.productos_api.service.CategoriaService;
import com.bricks.productos_api.service.ProductoService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;;

@Service
@AllArgsConstructor

public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;


    @Override
    @Transactional //Significa que haran transacciones con la base de datos.
    /*
    No podria recibir solo producto? ya que el id viene ahi dentro.
     */
    public Producto save(Long idCategoria, Producto producto) throws Exception {
        try {
            Categoria categoria = categoriaService.findById(idCategoria);
            producto.setCategory(categoria);
            return productoRepository.save(producto);
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private String filtrar (String name, Double price, Integer stock, Long categoryId ){
        if (name!=null) return "name";
        else if( price != null) return "price";
            else if (stock!= null) return "stock";
                else if (categoryId != null) return "category";
        return "default";
    }

    @Override
    //No uso @Transactional porque solo estoy leyendo.
    // Solo acepta un filtro a la vez.
    public List<Producto> findAll( String name, Double price, Integer stock,  Long categoryId
    )throws Exception{
        try {
            List<Producto> entities = new LinkedList<Producto>();
            String filtro = filtrar(name, price, stock, categoryId);
            switch (filtro) {
                case "name" -> entities = productoRepository.findByName(name);
                case "price" -> entities = productoRepository.findByPrice(price);
                case "stock" -> entities = productoRepository.findByStock(stock);
                case "category" -> entities = productoRepository.findByCategory_Id(categoryId);
                default -> entities = productoRepository.findAll();
            }
            ;
            return entities;
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    //Borra de la cache el producto modificado
    @CacheEvict( value = "productosCache", key = "#id")
    public Producto update(Long id, Producto producto)throws Exception {
        try {

            //Obtengo el producto a actualizar
            Optional<Producto> entityOptional = productoRepository.findById(id);
            Producto productoNuevo = entityOptional.get();

            //Lo actualizo con los datos ingresados
            productoNuevo.setName(producto.getName());
            productoNuevo.setPrice(producto.getPrice());
            productoNuevo.setStock(producto.getStock());
            productoNuevo.setCategory(producto.getCategory());

            //actualizo el producto
            return productoRepository.save(productoNuevo);
        } catch (Exception e) {
            throw new Exception((e.getMessage()));
        }
    }

    @Override
    @Transactional
    //Borra de la cache el producto modificado
    @CacheEvict( value = "productosCache", key = "#id")
    public boolean delete(Long id) throws Exception {
            try {
                if (productoRepository.existsById(id)) {
                    productoRepository.deleteById(id);
                    return true;
                }
                else{
                    throw new ResourceNotFoundException(" EL id no existe en la DB");
                }
            }catch(Exception e){
                    throw new Exception(e.getMessage());
                }
            }


    @Override
    @Transactional
    // Guarda el resultado en caché. La próxima vez, no ejecutará la consulta a la BD.
    @Cacheable(value = "productosCache", key = "#id")
    public Producto findById(Long id)throws Exception{
        try {
            Optional<Producto> entityOptional = productoRepository.findById(id);
            return entityOptional.get(); //Si es null tira una excepcion
        }
        catch(ResourceNotFoundException e){
            throw new ResourceNotFoundException("Producto con ID " +id +" no encontrado");
        }

    }
}
