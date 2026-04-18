package com.cursoapis.cursoapis.service;

import java.util.List;
import java.util.Optional;

import com.cursoapis.cursoapis.entity.EstadoProducto;
import com.cursoapis.cursoapis.entity.Producto;

public interface ProductoService {
Producto registraProducto(Producto producto);

List<Producto> listarProducto();

Optional <Producto> buscarPorNombre (String nombre);

Optional<Producto> buscarPorId (Long idProducto);

Producto actualizarProducto (Long idProducto, Producto producto);

void eliminarProducto(Long idProducto);

Producto cambiarEstadoProducto(Long idProducto, EstadoProducto nuevEstadoProducto);

List <Producto> obtenerProductosPorEstado(EstadoProducto estadoProducto);

}
