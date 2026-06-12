package com.cursoapis.cursoapis.config;

import com.cursoapis.cursoapis.dto.ProductoDTO;
import com.cursoapis.cursoapis.entity.Producto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        // Configuración para mapear de ProductoDTO a Producto (solo el campo estado/estadoProducto)
        modelMapper.typeMap(ProductoDTO.class, Producto.class).addMappings(mapper -> {
            mapper.map(ProductoDTO::getEstado, Producto::setEstadoProducto);
        });

        // Configuración para mapear de Producto a ProductoDTO (solo el campo estadoProducto/estado)
        modelMapper.typeMap(Producto.class, ProductoDTO.class).addMappings(mapper -> {
            mapper.map(Producto::getEstadoProducto, ProductoDTO::setEstado);
        });

        return modelMapper;
    }
}
