package com.cursoapis.cursoapis.controller;

import com.cursoapis.cursoapis.service.impl.ProductoServiceImpl;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cursoapis.cursoapis.entity.EstadoProducto;
import com.cursoapis.cursoapis.entity.Producto;
import com.cursoapis.cursoapis.service.ProductoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoServiceImpl productoServiceImpl;
    @Autowired
    private ProductoService productoService;

    ProductoController(ProductoServiceImpl productoServiceImpl) {
        this.productoServiceImpl = productoServiceImpl;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarProducto(@RequestBody Producto producto) {
        Producto nuevProducto = productoService.registraProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevProducto);

    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.listarProducto();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/buscarPornombre/{nombre}")
    public ResponseEntity<?> buscarPornombre(@PathVariable String nombre) {
        Optional<Producto> producto = productoService.buscarPorNombre(nombre);
        return producto.isPresent() ? ResponseEntity.ok(producto.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");

    }

    @GetMapping("/buscar/{idProducto}")
    public ResponseEntity<?> buscarPorIdProducto(@PathVariable Long idProducto) {
        Optional<Producto> producto = productoService.buscarPorId(idProducto);
        return producto.isPresent() ? ResponseEntity.ok(producto.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");

    }

    @PutMapping("/actualizar/{idProducto}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long idProducto, @RequestBody Producto producto) {
        try {
            Producto productoactualizado = new Producto();
            productoactualizado.setNombreProducto(producto.getNombreProducto());
            productoactualizado.setPrecio(producto.getPrecio());
            productoactualizado.setDescripcion(producto.getDescripcion());
            productoactualizado.setCantidad(producto.getCantidad());
            productoactualizado.setDescripcion(producto.getDescripcion());

            return ResponseEntity.ok(productoService.actualizarProducto(idProducto, productoactualizado));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
    }

    @DeleteMapping("/eliminar-producto/{idProducto}")
    public ResponseEntity<?> EliminarProducto(@PathVariable Long idProducto) {
        try {
            productoService.eliminarProducto(idProducto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/estado-producto/{idProducto}")
    public ResponseEntity <?> cambiarEstadoProducto(@PathVariable Long idProducto, @RequestBody EstadoProducto estadoProducto){
        try {
            return ResponseEntity.ok(productoService.cambiarEstadoProducto(idProducto, estadoProducto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/estado/{estadoProducto}")
    public ResponseEntity<List<Producto>> lisartProductoPorEstado(@PathVariable EstadoProducto estadoProducto){

        return ResponseEntity.ok(productoService.obtenerProductosPorEstado(estadoProducto));
    }
}
