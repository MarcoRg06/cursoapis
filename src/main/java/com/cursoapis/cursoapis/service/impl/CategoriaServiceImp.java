package com.cursoapis.cursoapis.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cursoapis.cursoapis.Exceptions.BadRequestException;
import com.cursoapis.cursoapis.Exceptions.ResourceNotFoundException;
import com.cursoapis.cursoapis.dto.CategoriaDTO;
import com.cursoapis.cursoapis.mapper.CategoriaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursoapis.cursoapis.entity.Categoria;
import com.cursoapis.cursoapis.repository.CategoriaRepository;
import com.cursoapis.cursoapis.service.CategoriaService;


@Service
public class CategoriaServiceImp implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;


    @Override
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        if (categoriaRepository.existsByNombreCategoria(categoriaDTO.getNombreCategoria())) {
            throw new BadRequestException("Ya existe una categoria con ese nombre ");
        }
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        return categoriaMapper.toDTO(categoriaRepository.save(categoria));

    }

    @Override
    public List<CategoriaDTO> listarCategoria() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(categoriaMapper::toDTO)
                .collect(Collectors.toList());

    }

    @Override
    public Optional<CategoriaDTO> obtenerCategoriaPorId(Long idCategoria) {
        Optional<Categoria> categoria = categoriaRepository.findById(idCategoria);
       return categoria.map(categoriaMapper::toDTO);
    }

    @Override
    public CategoriaDTO actualizarCategoria(Long idCategoria, CategoriaDTO categoriaDTO) {
        Categoria categoriaExistente = categoriaRepository.findById(idCategoria)
        .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));

        categoriaExistente.setNombreCategoria(categoriaDTO.getNombreCategoria());

        return categoriaMapper.toDTO(categoriaRepository.save(categoriaExistente));
    }

    @Override
    public void aliminarCategoria(Long idCategoria) {
      categoriaRepository.findById(idCategoria)
      .orElseThrow(()-> new ResourceNotFoundException("Categoria no encontrada para eliminar"));
    
      categoriaRepository.deleteById(idCategoria);
    }
    
}
