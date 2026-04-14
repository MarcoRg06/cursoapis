package com.cursoapis.cursoapis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cursoapis.cursoapis.entity.Message;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@RestController
@RequestMapping("/api/menssages")
public class MessageController {

    private List<Message> messages= new ArrayList<>();;

    public MessageController(){
        messages.add(new Message(1,"Hola mundo"));
        messages.add(new Message(2,"Hola mundo2"));
    }

    @GetMapping("/traer-mensajes")
    public List<Message> lsterMenajes(){
        return messages;
    }

    @GetMapping("traer-mensaje/{id}")
    public Message obtenerMensajePorID(@PathVariable int id){
        Optional <Message> message = messages.stream()
        .filter(m -> m.getId() == id )
        .findFirst();

        return message.orElse(null);
    }

    @PostMapping("/agregar-mensaje")
    public Message agregarMessage(@RequestBody Message message){
        messages.add(message);
        return message;
    }

    @DeleteMapping("/eiminar-mensaje/{id}")
    public void eleiminarMessage(@PathVariable int id){
        messages.removeIf(m -> m.getId() == id);
    }
    
}
