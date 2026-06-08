package com.cursoapis.cursoapis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cursoapis.cursoapis.entity.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
    Optional<Categoria> findByNombreCategoria(String nombreCategoria);

    boolean existsByNombreCategoria(String nombreCategoria);

}
