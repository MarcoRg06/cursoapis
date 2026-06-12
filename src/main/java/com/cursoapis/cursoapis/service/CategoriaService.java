package com.cursoapis.cursoapis.service;

import java.util.List;
import com.cursoapis.cursoapis.dto.CategoriaDTO;

public interface CategoriaService {
    
    CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO);
    
    List<CategoriaDTO> listarCategoria();

    CategoriaDTO obtenerCategoriaPorId(Long idCategoria);

    CategoriaDTO actualizarCategoria(Long idCategoria, CategoriaDTO categoriaDTO);

    void eliminarCategoria(Long idCategoria);
}
