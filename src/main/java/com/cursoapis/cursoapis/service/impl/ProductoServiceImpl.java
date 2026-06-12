package com.cursoapis.cursoapis.service.impl;

import java.util.List;

import com.cursoapis.cursoapis.exception.ResourceNotFoundException;
import com.cursoapis.cursoapis.dto.ProductoDTO;
import com.cursoapis.cursoapis.entity.Categoria;
import com.cursoapis.cursoapis.mapper.ProductoMapper;
import com.cursoapis.cursoapis.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cursoapis.cursoapis.entity.EstadoProducto;
import com.cursoapis.cursoapis.entity.Producto;
import com.cursoapis.cursoapis.repository.ProductoRepository;
import com.cursoapis.cursoapis.service.ProductoService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional
    public ProductoDTO registraProducto(Long categoriaId, ProductoDTO productoDTO) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria con ID " + categoriaId + " no encontrada"));

        Producto producto = productoMapper.toEntity(productoDTO);
        producto.setCategoria(categoria);

        return productoMapper.toDTO(productoRepository.save(producto));
    }

    @Override
    public List<ProductoDTO> listarProducto() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public ProductoDTO buscarPorNombre(String nombre) {
        return productoRepository.findByNombreProducto(nombre)
                .map(productoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con nombre " + nombre + " no encontrado"));
    }

    @Override
    public ProductoDTO buscarPorId(Long idProducto) {
        return productoRepository.findById(idProducto)
                .map(productoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con Id: " + idProducto + " no encontrado"));
    }

    @Override
    @Transactional
    public ProductoDTO actualizarProducto(Long idProducto, ProductoDTO productoDTO) {
        Producto productoExistente = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con Id: " + idProducto + " no encontrado"));

        // Mapeamos los campos básicos mediante el mapper existente
        productoMapper.toEntity(productoDTO, productoExistente);
        // Nos aseguramos de mantener la clave primaria del registro existente
        productoExistente.setIdProducto(idProducto);

        if (productoDTO.getCategoriaDTO() != null && productoDTO.getCategoriaDTO().getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaDTO().getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));
            productoExistente.setCategoria(categoria);
        }

        return productoMapper.toDTO(productoRepository.save(productoExistente));
    }

    @Override
    @Transactional
    public void eliminarProducto(Long idProducto) {
        if (!productoRepository.existsById(idProducto)) {
            throw new ResourceNotFoundException("Producto con Id: " + idProducto + " no encontrado");
        }
        productoRepository.deleteById(idProducto);
    }

    @Override
    @Transactional
    public ProductoDTO cambiarEstadoProducto(Long idProducto, EstadoProducto nuevEstadoProducto) {
        Producto productoExistente = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con Id: " + idProducto + " no encontrado"));

        productoExistente.setEstadoProducto(nuevEstadoProducto);

        return productoMapper.toDTO(productoRepository.save(productoExistente));
    }

    @Override
    public List<ProductoDTO> obtenerProductosPorEstado(EstadoProducto estadoProducto) {
        List<Producto> productos = productoRepository.findByEstadoProducto(estadoProducto);
        return productos.stream()
                .map(productoMapper::toDTO)
                .toList();
    }
}
