package com.cursoapis.cursoapis;

import com.cursoapis.cursoapis.entity.Producto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cursoapis.cursoapis.service.impl.ProductoServiceImpl;

public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoServiceImpl productoServiceImpl;

    @Autowired
    private ObjectMapper ObjectMapper;

    @Test
    public void traerProductos() throws Exception{
        Producto producto1 = new Producto();

    }

}
