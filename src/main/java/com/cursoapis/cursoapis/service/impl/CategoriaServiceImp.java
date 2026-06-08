package com.cursoapis.cursoapis.service.impl;

import java.util.List;
import java.util.Optional;

import com.cursoapis.cursoapis.Exceptions.BadRequestException;
import com.cursoapis.cursoapis.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursoapis.cursoapis.entity.Categoria;
import com.cursoapis.cursoapis.repository.CategoriaRepository;
import com.cursoapis.cursoapis.service.CategoriaService;

import lombok.SneakyThrows;

@Service
public class CategoriaServiceImp implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;


    @Override
    public Categoria crearCategoria(Categoria categoria) {
        if (categoriaRepository.existsByNombreCategoria(categoria.getNombreCategoria())) {
            throw new BadRequestException("Ya existe una categoria con ese nombre ");
        }
        return categoriaRepository.save(categoria);
    }

    @Override
    public List<Categoria> listarCategoria() {
        return categoriaRepository.findAll();
    }

    @Override
    public Optional<Categoria> obtenerCategoriaPorId(Long idCategoria) {
       return categoriaRepository.findById(idCategoria);
    }

    @Override
    public Categoria actualizarCategoria(Long idCategoria, Categoria categoria) {
        Categoria categoriaExistente = categoriaRepository.findById(idCategoria)
        .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));

        categoriaExistente.setNombreCategoria(categoria.getNombreCategoria());

        return categoriaRepository.save(categoriaExistente);
    }

    @Override
    public void aliminarCategoria(Long idCategoria) {
      categoriaRepository.findById(idCategoria)
      .orElseThrow(()-> new ResourceNotFoundException("Categoria no encontrada para eliminar"));
    
      categoriaRepository.deleteById(idCategoria);
    }
    
}
