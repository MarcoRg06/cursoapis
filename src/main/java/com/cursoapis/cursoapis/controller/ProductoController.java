package com.cursoapis.cursoapis.controller;

import com.cursoapis.cursoapis.dto.ProductoDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cursoapis.cursoapis.entity.EstadoProducto;
import com.cursoapis.cursoapis.service.ProductoService;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductoController {
    
    private final ProductoService productoService;

    @PostMapping("/ingresarProducto/{categoriaId}")
    public ResponseEntity<ProductoDTO> registroProducto(
            @PathVariable Long categoriaId,
            @Valid @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.registraProducto(categoriaId, productoDTO));
    }
    
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProducto());
    }

    @GetMapping("/buscarPornombre/{nombre}")
    public ResponseEntity<ProductoDTO> buscarPornombre(@PathVariable String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @GetMapping("/buscar/{idProducto}")
    public ResponseEntity<ProductoDTO> buscarPorIdProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(productoService.buscarPorId(idProducto));
    }

    @PutMapping("actualizarProducto/{idProducto}")
    public ResponseEntity<ProductoDTO> actualizarProducto(
            @PathVariable Long idProducto,
            @Valid @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.actualizarProducto(idProducto, productoDTO));
    }

    @DeleteMapping("/eliminarProducto/{idProducto}")
    public ResponseEntity<Void> EliminarProducto(@PathVariable Long idProducto) {
        productoService.eliminarProducto(idProducto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/estadoProducto/{idProducto}")
    public ResponseEntity<ProductoDTO> cambiarEstadoProducto(
            @PathVariable Long idProducto,
            @RequestBody EstadoProducto estadoProducto) {
        return ResponseEntity.ok(productoService.cambiarEstadoProducto(idProducto, estadoProducto));
    }

    @GetMapping("/estado/{estadoProducto}")
    public ResponseEntity<List<ProductoDTO>> lisartProductoPorEstado(@PathVariable EstadoProducto estadoProducto) {
        return ResponseEntity.ok(productoService.obtenerProductosPorEstado(estadoProducto));
    }
}
