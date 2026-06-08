package com.cursoapis.cursoapis.service;

import java.util.List;
import java.util.Optional;

import com.cursoapis.cursoapis.entity.Categoria;

public interface CategoriaService {
    
    Categoria crearCategoria (Categoria categoria);
    
    List<Categoria> listarCategoria();

    Optional <Categoria> obtenerCategoriaPorId(Long idCategoria);
    
    Categoria actualizarCategoria(Long idCategoria, Categoria categoria);

    void aliminarCategoria(Long idCategoria);


}
