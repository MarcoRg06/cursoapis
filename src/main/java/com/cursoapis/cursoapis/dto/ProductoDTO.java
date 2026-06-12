package com.cursoapis.cursoapis.dto;

import com.cursoapis.cursoapis.entity.EstadoProducto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {

    private Long idProducto;

    @NotBlank(message = "El nombre del producto no debe estar vacio")
    @Size(max = 100, message = "El nombre del producto no debe superar los 100 caracteres")
    private String nombreProducto;

    @Size(max = 255, message = "La descripcion no debe exceder los 255 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0.0")
    private BigDecimal precio;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El estado del producto es obligatorio")
    private EstadoProducto estado;

    @NotNull(message = "La categoria no puede estar vacia")
    private CategoriaDTO categoriaDTO;
}
