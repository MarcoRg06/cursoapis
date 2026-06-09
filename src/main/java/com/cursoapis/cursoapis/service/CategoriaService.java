package com.cursoapis.cursoapis.service;

import java.util.List;
import java.util.Optional;

import com.cursoapis.cursoapis.dto.CategoriaDTO;

public interface CategoriaService {
    
    CategoriaDTO crearCategoria (CategoriaDTO categoriaDTO);
    
    List<CategoriaDTO> listarCategoria();

    Optional <CategoriaDTO> obtenerCategoriaPorId(Long idCategoria);

    CategoriaDTO actualizarCategoria(Long idCategoria, CategoriaDTO categoriaDTO);

    void aliminarCategoria(Long idCategoria);


}
