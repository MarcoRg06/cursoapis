package com.cursoapis.cursoapis.dto;

import com.cursoapis.cursoapis.entity.Categoria;
import com.cursoapis.cursoapis.entity.EstadoProducto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {


    private Long idProducto;

    @NotNull(message = "El nombre del producto no debe estar vacio")
    @Size(max = 100, message = "El nombre del producto supera los 100 caracteres")
    private String nombreProducto;

    @Size(max = 255, message = "La descripcion no puede ecceder de los 255 caracteres")
    private String descripcion;

    @NotNull(message = "El precion es obligatorio")
    @Size(max = 0, message = "El precio debe ser mayor o igual a 0")
    private Double precio;

    @NotNull(message = "La cantidad es obligatoria")
    @Size(max = 1, message = "La cantidd debe ser almenos 1")
    private int cantidad;

    @NotNull(message = "El estado del producto es obligatorio")
    private EstadoProducto estado;

    private Categoria categoria;
}
