package com.cursoapis.cursoapis;

import com.cursoapis.cursoapis.controller.ProductoController;
import com.cursoapis.cursoapis.dto.CategoriaDTO;
import com.cursoapis.cursoapis.dto.ProductoDTO;
import com.cursoapis.cursoapis.entity.EstadoProducto;
import com.cursoapis.cursoapis.exception.ResourceNotFoundException;
import com.cursoapis.cursoapis.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    //Método para crear una Categoria
    private CategoriaDTO crearCategoriaDTO(Long id, String nombre) {
        return new CategoriaDTO(id, nombre);
    }

    //Método para crear un Producto
    private ProductoDTO crearProductoDTO(Long id, String nombre, String descripcion, BigDecimal precio, Integer cantidad, EstadoProducto estado, CategoriaDTO categoria) {
        return new ProductoDTO(id, nombre, descripcion, precio, cantidad, estado, categoria);
    }

    // POST Endpoints Tests
    //Registrar un producto exitosamente
    @Test
    @DisplayName("Debe registrar un producto exitosamente cuando los datos son válidos")
    void registrarProducto_DebeRetornarProductoCreado() throws Exception {
        Long idCategoria = 2L;
        CategoriaDTO categoria = crearCategoriaDTO(idCategoria, "Electrodomésticos");
        ProductoDTO nuevoProducto = crearProductoDTO(1L, "Licuadora", "con navajas de acero inoxidable", new BigDecimal("890"), 35, EstadoProducto.DISPONIBLE, categoria);

        when(productoService.registraProducto(eq(idCategoria), any(ProductoDTO.class)))
                .thenReturn(nuevoProducto);

        mockMvc.perform(post("/productos/ingresarProducto/{categoriaId}", idCategoria)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoProducto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProducto", is(1)))
                .andExpect(jsonPath("$.nombreProducto", is("Licuadora")))
                .andExpect(jsonPath("$.descripcion", is("con navajas de acero inoxidable")))
                .andExpect(jsonPath("$.precio", is(890)))
                .andExpect(jsonPath("$.cantidad", is(35)))
                .andExpect(jsonPath("$.estado", is("DISPONIBLE")));

        verify(productoService, times(1)).registraProducto(eq(idCategoria), any(ProductoDTO.class));
    }

    //Registrar un producto con datos inválidos
    @Test
    @DisplayName("Debe retornar 400 Bad Request cuando el producto a registrar no es válido")
    void registrarProducto_DebeRetornarBadRequest_CuandoDatosInvalidos() throws Exception {
        Long idCategoria = 2L;
        CategoriaDTO categoria = crearCategoriaDTO(idCategoria, "Electrodomésticos");
        // Producto inválido: nombre vacío y cantidad es menor que 1 (invalida la validación Bean Validation)
        ProductoDTO productoInvalido = crearProductoDTO(1L, "", "con navajas de acero inoxidable", new BigDecimal("890"), 0, EstadoProducto.DISPONIBLE, categoria);

        mockMvc.perform(post("/productos/ingresarProducto/{categoriaId}", idCategoria)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validación Fallida")))
                .andExpect(jsonPath("$.errors.nombreProducto", is("El nombre del producto no debe estar vacio")))
                .andExpect(jsonPath("$.errors.cantidad", is("La cantidad debe ser al menos 1")));

        verifyNoInteractions(productoService);
    }
    //Registrar un producto con categoría inexistente
    @Test
    @DisplayName("Debe retornar 404 Not Found al registrar si la categoría no existe")
    void registrarProducto_DebeRetornarNotFound_CuandoCategoriaNoExiste() throws Exception {
        Long idCategoria = 99L;
        CategoriaDTO categoria = crearCategoriaDTO(idCategoria, "Inexistente");
        ProductoDTO nuevoProducto = crearProductoDTO(1L, "Licuadora", "con navajas", new BigDecimal("890"), 35, EstadoProducto.DISPONIBLE, categoria);

        when(productoService.registraProducto(eq(idCategoria), any(ProductoDTO.class)))
                .thenThrow(new ResourceNotFoundException("Categoria con ID " + idCategoria + " no encontrada"));

        mockMvc.perform(post("/productos/ingresarProducto/{categoriaId}", idCategoria)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoProducto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Categoria con ID 99 no encontrada")))
                .andExpect(jsonPath("$.errorDetails", is("Recurso no encontrado")));
    }

    // GET Endpoints Tests
    //
    @Test
    @DisplayName("Debe listar todos los productos exitosamente")
    void listarProductos_DebeRetornarListaDeProductos() throws Exception {
        CategoriaDTO categoria1 = crearCategoriaDTO(3L, "Moda");
        ProductoDTO producto1 = crearProductoDTO(1L, "Camisa Blanca Eton", "Es de algodón egipcio", new BigDecimal("1200.00"), 200, EstadoProducto.DISPONIBLE, categoria1);

        CategoriaDTO categoria2 = crearCategoriaDTO(4L, "Moda");
        ProductoDTO producto2 = crearProductoDTO(2L, "Playera Blanca Eton", "Es de algodón egipcio", new BigDecimal("800.00"), 100, EstadoProducto.DISPONIBLE, categoria2);

        when(productoService.listarProducto()).thenReturn(Arrays.asList(producto1, producto2));

        mockMvc.perform(get("/productos/listarProductos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombreProducto", is("Camisa Blanca Eton")))
                .andExpect(jsonPath("$[1].nombreProducto", is("Playera Blanca Eton")));

        verify(productoService, times(1)).listarProducto();
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando no existen productos")
    void listarProductos_DebeRetornarListaVacia() throws Exception {
        when(productoService.listarProducto()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/productos/listarProductos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(productoService, times(1)).listarProducto();
    }

    @Test
    @DisplayName("Debe buscar un producto por su nombre exitosamente")
    void buscarProductoPorNombre_DebeRetornarProducto() throws Exception {
        CategoriaDTO categoria = crearCategoriaDTO(1L, "Moda");
        ProductoDTO producto = crearProductoDTO(1L, "Camisa-Eton", "Es de algodón egipcio", new BigDecimal("1200.00"), 200, EstadoProducto.DISPONIBLE, categoria);

        when(productoService.buscarPorNombre("Camisa-Eton")).thenReturn(producto);

        mockMvc.perform(get("/productos/buscarPornombre/{nombre}", "Camisa-Eton")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto", is("Camisa-Eton")));

        verify(productoService, times(1)).buscarPorNombre("Camisa-Eton");
    }

    @Test
    @DisplayName("Debe retornar 404 Not Found al buscar un producto inexistente por nombre")
    void buscarProductoPorNombre_DebeRetornarNotFound_CuandoNoExiste() throws Exception {
        String nombre = "Inexistente";
        when(productoService.buscarPorNombre(nombre))
                .thenThrow(new ResourceNotFoundException("Producto con nombre " + nombre + " no encontrado"));

        mockMvc.perform(get("/productos/buscarPornombre/{nombre}", nombre)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Producto con nombre Inexistente no encontrado")));
    }

    @Test
    @DisplayName("Debe buscar un producto por su ID exitosamente")
    void buscarProductoPorId_DebeRetornarProducto() throws Exception {
        Long idBuscar = 1L;
        CategoriaDTO categoria = crearCategoriaDTO(idBuscar, "Moda");
        ProductoDTO producto = crearProductoDTO(idBuscar, "Camisa Blanca Eton", "Es de algodón egipcio", new BigDecimal("1200.00"), 200, EstadoProducto.DISPONIBLE, categoria);

        when(productoService.buscarPorId(idBuscar)).thenReturn(producto);

        mockMvc.perform(get("/productos/buscar/{idProducto}", idBuscar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto", is(1)))
                .andExpect(jsonPath("$.nombreProducto", is("Camisa Blanca Eton")));

        verify(productoService, times(1)).buscarPorId(idBuscar);
    }

    @Test
    @DisplayName("Debe retornar 404 Not Found al buscar un producto inexistente por ID")
    void buscarProductoPorId_DebeRetornarNotFound_CuandoNoExiste() throws Exception {
        Long idBuscar = 99L;
        when(productoService.buscarPorId(idBuscar))
                .thenThrow(new ResourceNotFoundException("Producto con Id: " + idBuscar + " no encontrado"));

        mockMvc.perform(get("/productos/buscar/{idProducto}", idBuscar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Producto con Id: 99 no encontrado")));
    }

    //  PUT Endpoints Tests

    @Test
    @DisplayName("Debe actualizar un producto exitosamente cuando existe")
    void actualizarProducto_DebeRetornarProductoActualizado() throws Exception {
        Long id = 1L;
        CategoriaDTO categoria = crearCategoriaDTO(3L, "Moda");
        ProductoDTO productoNuevo = crearProductoDTO(id, "Camisa Blanca", "Es de algodón", new BigDecimal("500.00"), 500, EstadoProducto.DISPONIBLE, categoria);

        when(productoService.actualizarProducto(eq(id), any(ProductoDTO.class)))
                .thenReturn(productoNuevo);

        mockMvc.perform(put("/productos/actualizarProducto/{idProducto}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productoNuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto", is(1)))
                .andExpect(jsonPath("$.nombreProducto", is("Camisa Blanca")))
                .andExpect(jsonPath("$.cantidad", is(500)));

        verify(productoService, times(1)).actualizarProducto(eq(id), any(ProductoDTO.class));
    }

    @Test
    @DisplayName("Debe retornar 404 Not Found al actualizar un producto inexistente")
    void actualizarProducto_DebeRetornarNotFound_CuandoNoExiste() throws Exception {
        Long id = 99L;
        CategoriaDTO categoria = crearCategoriaDTO(3L, "Moda");
        ProductoDTO productoNuevo = crearProductoDTO(id, "Camisa Blanca", "Es de algodón", new BigDecimal("500.00"), 500, EstadoProducto.DISPONIBLE, categoria);

        when(productoService.actualizarProducto(eq(id), any(ProductoDTO.class)))
                .thenThrow(new ResourceNotFoundException("Producto con Id: " + id + " no encontrado"));

        mockMvc.perform(put("/productos/actualizarProducto/{idProducto}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoNuevo)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Producto con Id: 99 no encontrado")));
    }

    @Test
    @DisplayName("Debe cambiar el estado de un producto exitosamente")
    void cambiarEstadoProducto_DebeRetornarProductoActualizado() throws Exception {
        Long id = 1L;
        CategoriaDTO categoria = crearCategoriaDTO(3L, "Moda");
        ProductoDTO producto = crearProductoDTO(id, "Camisa Blanca", "Es de algodón", new BigDecimal("500.00"), 500, EstadoProducto.DISPONIBLE, categoria);

        when(productoService.cambiarEstadoProducto(eq(id), eq(EstadoProducto.DISPONIBLE)))
                .thenReturn(producto);

        mockMvc.perform(put("/productos/estadoProducto/{idProducto}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(EstadoProducto.DISPONIBLE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto", is(1)))
                .andExpect(jsonPath("$.estado", is("DISPONIBLE")));

        verify(productoService, times(1)).cambiarEstadoProducto(eq(id), eq(EstadoProducto.DISPONIBLE));
    }

    // DELETE Endpoints Tests

    @Test
    @DisplayName("Debe eliminar un producto exitosamente")
    void eliminarProducto_DebeRetornarNoContent() throws Exception {
        Long idProductoParaEliminar = 1L;

        doNothing().when(productoService).eliminarProducto(idProductoParaEliminar);

        mockMvc.perform(delete("/productos/eliminarProducto/{idProducto}", idProductoParaEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).eliminarProducto(idProductoParaEliminar);
    }

    @Test
    @DisplayName("Debe retornar 404 Not Found al eliminar un producto inexistente")
    void eliminarProducto_DebeRetornarNotFound_CuandoNoExiste() throws Exception {
        Long idProductoParaEliminar = 99L;

        doThrow(new ResourceNotFoundException("Producto con Id: " + idProductoParaEliminar + " no encontrado"))
                .when(productoService).eliminarProducto(idProductoParaEliminar);

        mockMvc.perform(delete("/productos/eliminarProducto/{idProducto}", idProductoParaEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Producto con Id: 99 no encontrado")));
    }

    // GET Filter Tests

    @Test
    @DisplayName("Debe listar productos por estado exitosamente")
    void listarProductosPorEstado_DebeRetornarProductosFiltrados() throws Exception {

        CategoriaDTO categoria1 = crearCategoriaDTO(3L, "Moda");
        ProductoDTO producto1 = crearProductoDTO(1L, "Camisa Blanca Eton", "Es de algodón egipcio", new BigDecimal("1200.00"), 200, EstadoProducto.DISPONIBLE, categoria1);

        CategoriaDTO categoria2 = crearCategoriaDTO(4L, "Moda");
        ProductoDTO producto2 = crearProductoDTO(2L, "Playera Blanca Eton", "Es de algodón egipcio", new BigDecimal("800.00"), 100, EstadoProducto.DISPONIBLE, categoria2);

        when(productoService.obtenerProductosPorEstado(EstadoProducto.DISPONIBLE))
                .thenReturn(Arrays.asList(producto1, producto2));

        mockMvc.perform(get("/productos/estado/{estadoProducto}", EstadoProducto.DISPONIBLE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombreProducto", is("Camisa Blanca Eton")))
                .andExpect(jsonPath("$[1].nombreProducto", is("Playera Blanca Eton")));

        verify(productoService, times(1)).obtenerProductosPorEstado(EstadoProducto.DISPONIBLE);
    }
}