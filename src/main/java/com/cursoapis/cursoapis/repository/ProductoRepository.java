package com.cursoapis.cursoapis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursoapis.cursoapis.entity.EstadoProducto;
import com.cursoapis.cursoapis.entity.Producto;

public interface ProductoRepository extends JpaRepository <Producto, Long> {

    Optional<Producto> findByNombreProducto (String  nombreProducto);

    List<Producto> findByEstadoProducto (EstadoProducto estadoProducto);

}
