package com.cursoapis.cursoapis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {
    
    private Long idCategoria;

    @NotBlank(message = "El nombre de la categoria no debe estar vacio")
    @Size(min = 3, max = 50, message = "El nombre de la categoria debe tener entre 3 y 50 caracteres")
    private String nombreCategoria;
}
