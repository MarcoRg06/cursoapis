package com.cursoapis.cursoapis.controller;

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
import com.cursoapis.cursoapis.entity.Categoria;
import com.cursoapis.cursoapis.service.CategoriaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/categoria")
@CrossOrigin(origins = "*")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/crearCategoria")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoria(categoria));
    }

    @GetMapping("/listarCategoria")
   public ResponseEntity<List<Categoria>> listarCategoria(){
    List <Categoria> categorias = categoriaService.listarCategoria();
    return new ResponseEntity<>(categorias, HttpStatus.OK);

   }

   @GetMapping("/obtenerCategoriaPorId/{idCategoria}")
   public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long idCategoria) throws Exception{
    Optional <Categoria> categoria1Optional = categoriaService.obtenerCategoriaPorId(idCategoria);
    if (categoria1Optional.isPresent()) {
        return new ResponseEntity<>(categoria1Optional.get(),HttpStatus.OK);
    }else {
        throw new Exception("Categoria no encontrada");
    }
   }

    @PutMapping("/actualizarCategoria/{idCategoria}")
    public ResponseEntity <Categoria> actualizarCategoria (@PathVariable Long idCategoria, @RequestBody Categoria categoria){
        try {
            Categoria categoriaActualizada = categoriaService.actualizarCategoria(idCategoria,categoria);
            if (categoriaActualizada != null) {
                return new ResponseEntity<>(categoriaActualizada,HttpStatus.OK);
            }else{
                throw new Exception("Categoria no encontrada para actualizar");
            }

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

   @DeleteMapping("/eliminarCategoria/{idCategoria}")
   public ResponseEntity<Void> eliminarCategoria(@PathVariable Long idCategoria) {
    try{
        categoriaService.aliminarCategoria(idCategoria);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }catch (Exception e){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
   }
}
