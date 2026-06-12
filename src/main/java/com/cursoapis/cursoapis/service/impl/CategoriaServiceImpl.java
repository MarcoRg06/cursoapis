package com.cursoapis.cursoapis.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.cursoapis.cursoapis.exception.BadRequestException;
import com.cursoapis.cursoapis.exception.ResourceNotFoundException;
import com.cursoapis.cursoapis.dto.CategoriaDTO;
import com.cursoapis.cursoapis.mapper.CategoriaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cursoapis.cursoapis.entity.Categoria;
import com.cursoapis.cursoapis.repository.CategoriaRepository;
import com.cursoapis.cursoapis.service.CategoriaService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    @Transactional
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        if (categoriaRepository.existsByNombreCategoria(categoriaDTO.getNombreCategoria())) {
            throw new BadRequestException("Ya existe una categoria con ese nombre");
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
    public CategoriaDTO obtenerCategoriaPorId(Long idCategoria) {
        return categoriaRepository.findById(idCategoria)
                .map(categoriaMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria con ID " + idCategoria + " no encontrada"));
    }

    @Override
    @Transactional
    public CategoriaDTO actualizarCategoria(Long idCategoria, CategoriaDTO categoriaDTO) {
        Categoria categoriaExistente = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria con ID " + idCategoria + " no encontrada"));

        categoriaExistente.setNombreCategoria(categoriaDTO.getNombreCategoria());

        return categoriaMapper.toDTO(categoriaRepository.save(categoriaExistente));
    }

    @Override
    @Transactional
    public void eliminarCategoria(Long idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new ResourceNotFoundException("Categoria con ID " + idCategoria + " no encontrada para eliminar");
        }
        categoriaRepository.deleteById(idCategoria);
    }
}
