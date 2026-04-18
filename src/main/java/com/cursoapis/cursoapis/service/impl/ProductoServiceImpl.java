package com.cursoapis.cursoapis.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursoapis.cursoapis.entity.EstadoProducto;
import com.cursoapis.cursoapis.entity.Producto;
import com.cursoapis.cursoapis.repository.ProductoRepository;
import com.cursoapis.cursoapis.service.ProductoService;

import lombok.SneakyThrows;

@Service
public class ProductoServiceImpl implements ProductoService{

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Producto registraProducto(Producto producto) {
       return productoRepository.save(producto);
    }

    @Override
    public List<Producto> listarProducto() {
       return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> buscarPorNombre(String nombre) {
       return productoRepository.findByNombreProducto(nombre);
    }

    @Override
    public Optional<Producto> buscarPorId(Long idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Override
    @SneakyThrows
    public Producto actualizarProducto(Long idProducto, Producto producto) {
        Producto productoExistente = productoRepository.findById(idProducto)
        .orElseThrow(()-> new Exception("Producto con Id: "+ idProducto + " no encontrado"));
        
        productoExistente.setNombreProducto(producto.getNombreProducto());
        productoExistente.setPrecio(productoExistente.getPrecio());
        productoExistente.setCantidad(productoExistente.getCantidad());
        productoExistente.setDescripcion(productoExistente.getDescripcion());
        
        return productoRepository.save(productoExistente);
    }

    @Override
    @SneakyThrows
    public void eliminarProducto(Long idProducto) {
      productoRepository.findById(idProducto)
        .orElseThrow(()-> new Exception("Producto con Id: "+ idProducto + " no encontrado"));
        
         productoRepository.deleteById(idProducto);
    }

    @Override
    @SneakyThrows
    public Producto cambiarEstadoProducto(Long idProducto, EstadoProducto nuevEstadoProducto) {
         Producto productoExistente = productoRepository.findById(idProducto)
        .orElseThrow(()-> new Exception("Producto con Id: "+ idProducto + " no encontrado"));

        productoExistente.setEstadoProducto(nuevEstadoProducto);
        return productoRepository.save(productoExistente);
    }

    @Override
    public List<Producto> obtenerProductosPorEstado(EstadoProducto estadoProducto) {
        return productoRepository.findByEstadoProducto(estadoProducto);
    }

}
