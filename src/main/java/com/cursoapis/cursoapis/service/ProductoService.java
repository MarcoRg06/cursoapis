package com.cursoapis.cursoapis.service;

import java.util.List;

import com.cursoapis.cursoapis.dto.ProductoDTO;
import com.cursoapis.cursoapis.entity.EstadoProducto;

public interface ProductoService {

    ProductoDTO registraProducto(Long categoriaId, ProductoDTO productoDTO);

    List<ProductoDTO> listarProducto();

    ProductoDTO buscarPorNombre(String nombre);

    ProductoDTO buscarPorId(Long idProducto);

    ProductoDTO actualizarProducto(Long idProducto, ProductoDTO productoDTO);

    void eliminarProducto(Long idProducto);

    ProductoDTO cambiarEstadoProducto(Long idProducto, EstadoProducto nuevEstadoProducto);

    List<ProductoDTO> obtenerProductosPorEstado(EstadoProducto estadoProducto);
}
