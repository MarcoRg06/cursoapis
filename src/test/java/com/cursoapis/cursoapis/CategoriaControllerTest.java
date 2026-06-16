package com.cursoapis.cursoapis;

import com.cursoapis.cursoapis.controller.CategoriaController;
import com.cursoapis.cursoapis.dto.CategoriaDTO;
import com.cursoapis.cursoapis.exception.ResourceNotFoundException;
import com.cursoapis.cursoapis.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoriaController.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    //Metodo para crear una categoria
    private CategoriaDTO crearCategoriaDTO(Long id, String nombre) {
        return new CategoriaDTO(id, nombre);
    }


    @Test
    @DisplayName("Debe crear una categoría exitosamente cuando los datos son válidos")
    void crearCategoria_DebeRetornarCategoriaCreada() throws Exception {
        CategoriaDTO categoria = crearCategoriaDTO(1L, "Calzado");

        // Usamos any(CategoriaDTO.class) para evitar fallos de matching por la falta de equals/hashCode
        when(categoriaService.crearCategoria(any(CategoriaDTO.class)))
                .thenReturn(categoria);

        mockMvc.perform(post("/categoria/crearCategoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCategoria", is(1)))
                .andExpect(jsonPath("$.nombreCategoria", is("Calzado")));

        verify(categoriaService, times(1)).crearCategoria(any(CategoriaDTO.class));
    }

    @Test
    @DisplayName("Debe retornar 400 Bad Request al crear una categoría con nombre inválido")
    void crearCategoria_DebeRetornarBadRequest_CuandoNombreInvalido() throws Exception {
        // Categoría inválida: nombre vacío y menor de 3 caracteres
        CategoriaDTO categoriaInvalida = crearCategoriaDTO(1L, "Ca");

        mockMvc.perform(post("/categoria/crearCategoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaInvalida)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validación Fallida")))
                .andExpect(jsonPath("$.errors.nombreCategoria", is("El nombre de la categoria debe tener entre 3 y 50 caracteres")));

        verifyNoInteractions(categoriaService);
    }

    @Test
    @DisplayName("Debe listar todas las categorías exitosamente")
    void listarCategorias_DebeRetornarListaDeCategorias() throws Exception {
        CategoriaDTO categoria1 = crearCategoriaDTO(1L, "Calzado");
        CategoriaDTO categoria2 = crearCategoriaDTO(2L, "Pantallas");

        when(categoriaService.listarCategoria()).thenReturn(Arrays.asList(categoria1, categoria2));

        mockMvc.perform(get("/categoria/listarCategoria")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombreCategoria", is("Calzado")))
                .andExpect(jsonPath("$[1].nombreCategoria", is("Pantallas")));

        verify(categoriaService, times(1)).listarCategoria();
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando no existen categorías")
    void listarCategorias_DebeRetornarListaVacia() throws Exception {
        when(categoriaService.listarCategoria()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/categoria/listarCategoria")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(categoriaService, times(1)).listarCategoria();
    }

    @Test
    @DisplayName("Debe obtener una categoría por su ID exitosamente")
    void obtenerCategoriaPorId_DebeRetornarCategoria() throws Exception {
        Long id = 1L;
        CategoriaDTO categoria = crearCategoriaDTO(1L, "Calzado");

        when(categoriaService.obtenerCategoriaPorId(id)).thenReturn(categoria);

        mockMvc.perform(get("/categoria/obtenerCategoriaPorId/{idCategoria}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria", is(1)))
                .andExpect(jsonPath("$.nombreCategoria", is("Calzado")));

        verify(categoriaService, times(1)).obtenerCategoriaPorId(id);
    }

    @Test
    @DisplayName("Debe retornar 404 Not Found al buscar una categoría que no existe")
    void obtenerCategoriaPorId_DebeRetornarNotFound_CuandoNoExiste() throws Exception {
        Long id = 99L;
        when(categoriaService.obtenerCategoriaPorId(id))
                .thenThrow(new ResourceNotFoundException("Categoria con ID " + id + " no encontrada"));

        mockMvc.perform(get("/categoria/obtenerCategoriaPorId/{idCategoria}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Categoria con ID 99 no encontrada")));
    }


    @Test
    @DisplayName("Debe actualizar una categoría exitosamente cuando existe")
    void actualizarCategoria_DebeRetornarCategoriaActualizada() throws Exception {
        Long idCategoria = 1L;
        CategoriaDTO categoriaNueva = crearCategoriaDTO(idCategoria, "Embutidos");

        when(categoriaService.actualizarCategoria(eq(idCategoria), any(CategoriaDTO.class)))
                .thenReturn(categoriaNueva);

        mockMvc.perform(put("/categoria/actualizarCategoria/{idCategoria}", idCategoria)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaNueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria", is(1)))
                .andExpect(jsonPath("$.nombreCategoria", is("Embutidos")));

        verify(categoriaService, times(1)).actualizarCategoria(eq(idCategoria), any(CategoriaDTO.class));
    }

    @Test
    @DisplayName("Debe retornar 404 Not Found al actualizar una categoría inexistente")
    void actualizarCategoria_DebeRetornarNotFound_CuandoNoExiste() throws Exception {
        Long idCategoria = 99L;
        CategoriaDTO categoriaNueva = crearCategoriaDTO(idCategoria, "Embutidos");

        when(categoriaService.actualizarCategoria(eq(idCategoria), any(CategoriaDTO.class)))
                .thenThrow(new ResourceNotFoundException("Categoria con ID " + idCategoria + " no encontrada"));

        mockMvc.perform(put("/categoria/actualizarCategoria/{idCategoria}", idCategoria)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaNueva)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Categoria con ID 99 no encontrada")));
    }

    @Test
    @DisplayName("Debe eliminar una categoría exitosamente")
    void eliminarCategoria_DebeRetornarNoContent() throws Exception {
        Long idCategoriaParaEliminar = 1L;

        doNothing().when(categoriaService).eliminarCategoria(idCategoriaParaEliminar);

        mockMvc.perform(delete("/categoria/eliminarCategoria/{idCategoria}", idCategoriaParaEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).eliminarCategoria(idCategoriaParaEliminar);
    }

    @Test
    @DisplayName("Debe retornar 404 Not Found al eliminar una categoría inexistente")
    void eliminarCategoria_DebeRetornarNotFound_CuandoNoExiste() throws Exception {
        Long idCategoriaParaEliminar = 99L;

        doThrow(new ResourceNotFoundException("Categoria con ID " + idCategoriaParaEliminar + " no encontrada"))
                .when(categoriaService).eliminarCategoria(idCategoriaParaEliminar);

        mockMvc.perform(delete("/categoria/eliminarCategoria/{idCategoria}", idCategoriaParaEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Categoria con ID 99 no encontrada")));
    }

    @Test
    @DisplayName("Debe retornar 409 Conflict al eliminar una categoría en uso")
    void eliminarCategoria_DebeRetornarConflict_CuandoTieneProductosAsociados() throws Exception {
        Long idCategoriaParaEliminar = 1L;

        doThrow(new DataIntegrityViolationException("Conflict: Category in use"))
                .when(categoriaService).eliminarCategoria(idCategoriaParaEliminar);

        mockMvc.perform(delete("/categoria/eliminarCategoria/{idCategoria}", idCategoriaParaEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode", is(409)))
                .andExpect(jsonPath("$.message", is("Conflicto de integridad de datos. Verifique si el registro ya existe o si tiene relaciones activas.")))
                .andExpect(jsonPath("$.errorDetails", is("Conflicto de base de datos")));
    }
}
