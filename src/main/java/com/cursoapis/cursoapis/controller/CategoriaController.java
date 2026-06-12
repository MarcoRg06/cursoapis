package com.cursoapis.cursoapis.controller;

import java.util.List;

import com.cursoapis.cursoapis.dto.CategoriaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cursoapis.cursoapis.service.CategoriaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoriaController {
    
    private final CategoriaService categoriaService;

    @PostMapping("/crearCategoria")
    public ResponseEntity<CategoriaDTO> crearCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoria(categoriaDTO));
    }

    @GetMapping("/listarCategoria")
    public ResponseEntity<List<CategoriaDTO>> listarCategoria() {
        List<CategoriaDTO> categorias = categoriaService.listarCategoria();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/obtenerCategoriaPorId/{idCategoria}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(@PathVariable Long idCategoria) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriaPorId(idCategoria));
    }

    @PutMapping("/actualizarCategoria/{idCategoria}")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(
            @PathVariable Long idCategoria,
            @Valid @RequestBody CategoriaDTO categoriaDTO) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(idCategoria, categoriaDTO));
    }

    @DeleteMapping("/eliminarCategoria/{idCategoria}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long idCategoria) {
        categoriaService.eliminarCategoria(idCategoria);
        return ResponseEntity.noContent().build();
    }
}
