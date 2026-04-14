package com.cursoapis.cursoapis.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/*
controller: es una clase de java que gestiona las solicitudes HTTP (GET, POST, DELETE, ACTUALIZAR)
que llega a la aplicacion web API, es el puente entre el cliente y la logica de negocio
  Controladores REST: Este es una clase en Java que expone rutas http (URLS) 
  para que otros sistemas puedan enviarle peticiones y recibir respuestas en formato JSON
  - Inidica que esta clase es un controller REST
  - Combina @Controller y @ResponseBody
  - No vistas HTML
*/

@RestController //indicamos que esta clase es un controller Web en APIs REST 
@RequestMapping("/micontroller") //Configuramos una URL para todos los metodos del controller
public class PruebaController {

    @GetMapping("/saludo")
    public String metodoSaludar(){
        return "Hola Mundo";
    }

}
