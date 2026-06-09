package com.cursoapis.cursoapis.service.impl;

import java.util.List;
import java.util.Optional;

import com.cursoapis.cursoapis.Exceptions.BadRequestException;
import com.cursoapis.cursoapis.Exceptions.ResourceNotFoundException;
import com.cursoapis.cursoapis.dto.ProductoDTO;
import com.cursoapis.cursoapis.entity.Categoria;
import com.cursoapis.cursoapis.mapper.ProductoMapper;
import com.cursoapis.cursoapis.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursoapis.cursoapis.entity.EstadoProducto;
import com.cursoapis.cursoapis.entity.Producto;
import com.cursoapis.cursoapis.repository.ProductoRepository;
import com.cursoapis.cursoapis.service.ProductoService;


@Service
public class ProductoServiceImpl implements ProductoService{

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoMapper productoMapper;

    @Override
    public ProductoDTO registraProducto(Long categoriaId, ProductoDTO productoDTO) {
       Categoria categoria = categoriaRepository.findById(categoriaId)
               .orElseThrow(() -> new ResourceNotFoundException("Categoria con ID "+ categoriaId+" no encontrada"));
       if (productoDTO.getPrecio() == null || productoDTO.getPrecio()<=0){
           throw new BadRequestException("El precio del producto debe ser mayor que cero");
       }

       Producto producto = productoMapper.toEntity(productoDTO);
        producto.setCategoria(categoria);

       return productoMapper.toDTO(productoRepository.save(producto));

    }

    @Override
    public List<ProductoDTO> listarProducto() {
        List <Producto> productos = productoRepository.findAll();
        return productos.stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<ProductoDTO> buscarPorNombre(String nombre) {
       Optional<Producto> producto = productoRepository.findByNombreProducto(nombre);
       return producto.map(productoMapper::toDTO);
    }

    @Override
    public Optional<ProductoDTO> buscarPorId(Long idProducto) {
        Optional<Producto> producto = productoRepository.findById(idProducto);
        return producto.map(productoMapper::toDTO);
    }

    @Override
    public ProductoDTO actualizarProducto(Long idProducto, ProductoDTO productoDTO) {
        Producto productoExistente = productoRepository.findById(idProducto)
        .orElseThrow(()-> new ResourceNotFoundException("Producto con Id: "+ idProducto + " no encontrado"));

        productoExistente.setNombreProducto(productoDTO.getNombreProducto());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setCantidad(productoDTO.getCantidad());
        productoExistente.setDescripcion(productoDTO.getDescripcion());
        productoExistente.setEstadoProducto(productoDTO.getEstado());

        if (productoDTO.getCategoria() != null && productoDTO.getCategoria().getIdCategoria() != null){
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException ("Categoria no encontrada"));
            productoExistente.setCategoria(categoria);
        }
        return productoMapper.toDTO(productoRepository.save(productoExistente));
    }

    @Override

    public void eliminarProducto(Long idProducto) {
      productoRepository.findById(idProducto)
        .orElseThrow(()-> new ResourceNotFoundException("Producto con Id: "+ idProducto + " no encontrado"));
        
         productoRepository.deleteById(idProducto);
    }

    @Override

    public ProductoDTO cambiarEstadoProducto(Long idProducto, EstadoProducto nuevEstadoProducto) {
         Producto productoExistente = productoRepository.findById(idProducto)
        .orElseThrow(()-> new ResourceNotFoundException("Producto con Id: "+ idProducto + " no encontrado"));

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
