package com.cursoapis.cursoapis.service.impl;

import java.util.List;
import java.util.Optional;

import com.cursoapis.cursoapis.entity.Categoria;
import com.cursoapis.cursoapis.repository.CategoriaRepository;
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

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @SneakyThrows
    public Producto registraProducto(Long categoriaId, Producto producto) {
       Categoria categoria = categoriaRepository.findById(categoriaId)
               .orElseThrow(() -> new Exception ("Categoria con ID "+ categoriaId+" no encontrada"));
       producto.setCategoria(categoria);
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
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setCantidad(producto.getCantidad());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setEstadoProducto(producto.getEstadoProducto());

        if (producto.getCategoria() != null && producto.getCategoria().getIdCategoria() != null){
            Categoria categoria = categoriaRepository.findById(producto.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new Exception ("Categoria no encontrada"));
            productoExistente.setCategoria(categoria);
        }
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
