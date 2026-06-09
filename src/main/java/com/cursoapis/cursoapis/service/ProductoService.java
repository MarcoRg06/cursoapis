package com.cursoapis.cursoapis.service;

import java.util.List;
import java.util.Optional;

import com.cursoapis.cursoapis.dto.ProductoDTO;
import com.cursoapis.cursoapis.entity.EstadoProducto;

public interface ProductoService {

ProductoDTO registraProducto(Long categoriaId, ProductoDTO productoDTO);

List<ProductoDTO> listarProducto();

Optional <ProductoDTO> buscarPorNombre (String nombre);

Optional<ProductoDTO> buscarPorId (Long idProducto);

ProductoDTO actualizarProducto (Long idProducto, ProductoDTO productoDTO);

void eliminarProducto(Long idProducto);

ProductoDTO cambiarEstadoProducto(Long idProducto, EstadoProducto nuevEstadoProducto);

List <ProductoDTO> obtenerProductosPorEstado(EstadoProducto estadoProducto);

}
