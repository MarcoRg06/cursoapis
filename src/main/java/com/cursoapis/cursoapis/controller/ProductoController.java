package com.cursoapis.cursoapis.controller;

import com.cursoapis.cursoapis.service.impl.ProductoServiceImpl;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cursoapis.cursoapis.entity.EstadoProducto;
import com.cursoapis.cursoapis.entity.Producto;
import com.cursoapis.cursoapis.service.ProductoService;

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

    @PostMapping("/registrar/{categoriaId}")
    public ResponseEntity<?> registrarProducto(
            @PathVariable Long categoriaId,
            @RequestParam("nombreProducto") String nombreProducto,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("cantidad") int cantidad,
            @RequestParam("estado") EstadoProducto estado) {
        Producto producto = new Producto();
        producto.setNombreProducto(nombreProducto);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setCantidad(cantidad);
        producto.setEstadoProducto(estado);

        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.registraProducto(categoriaId, producto));

    }

    // @GetMapping
    // public ResponseEntity<List<Producto>> listarProductos() {
    // return ResponseEntity.ok(productoService.listarProducto());
    // }

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarProducto();
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
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long idProducto,
            @RequestParam("nombreProducto") String nombreProducto,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("cantidad") int cantidad,
            @RequestParam("estado") EstadoProducto estado) {
        try {

            Producto producto = new Producto();
            producto.setNombreProducto(nombreProducto);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setCantidad(cantidad);
            producto.setEstadoProducto(estado);

            return ResponseEntity.ok(productoService.actualizarProducto(idProducto, producto));
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
    public ResponseEntity<?> cambiarEstadoProducto(@PathVariable Long idProducto,
            @RequestBody EstadoProducto estadoProducto) {
        try {
            return ResponseEntity.ok(productoService.cambiarEstadoProducto(idProducto, estadoProducto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/estado/{estadoProducto}")
    public ResponseEntity<List<Producto>> lisartProductoPorEstado(@PathVariable EstadoProducto estadoProducto) {

        return ResponseEntity.ok(productoService.obtenerProductosPorEstado(estadoProducto));
    }
}
