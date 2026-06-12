package com.cursoapis.cursoapis.mapper;

import com.cursoapis.cursoapis.dto.ProductoDTO;
import com.cursoapis.cursoapis.entity.Producto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductoMapper {

    private final ModelMapper modelMapper;

    public Producto toEntity(ProductoDTO productoDTO){
        return modelMapper.map(productoDTO, Producto.class);
    }

    public void toEntity(ProductoDTO productoDTO, Producto productoExistente){
        modelMapper.map(productoDTO, productoExistente);
    }

    public ProductoDTO toDTO(Producto producto) {
        return modelMapper.map(producto, ProductoDTO.class);
    }
}
