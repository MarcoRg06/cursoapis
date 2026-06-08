package com.cursoapis.cursoapis.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
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
    @SneakyThrows
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
    @SneakyThrows
    public Categoria actualizarCategoria(Long idCategoria, Categoria categoria) {
        Categoria categoriaExistente = categoriaRepository.findById(idCategoria)
        .orElseThrow(() -> new Exception("Producto con Id: "+ idCategoria+ " no encontrada"));

        categoriaExistente.setNombreCategoria(categoria.getNombreCategoria());

        return categoriaRepository.save(categoriaExistente);
    }

    @Override
    @SneakyThrows
    public void aliminarCategoria(Long idCategoria) {
      categoriaRepository.findById(idCategoria)
      .orElseThrow(()-> new Exception("Producto con Id: "+ idCategoria + "no encontrado"));
    
      categoriaRepository.deleteById(idCategoria);
    }
    
}
