package com.cursoapis.cursoapis.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


//Se convierte en un manejador global de errores para todos los controladores
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Indiccamos la excepcion que manejará el metodo
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado");
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    //Indiccamos la excepcion que manejará el metodo
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception){
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud incorrecta");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    //Indiccamos la excepcion que manejará el metodo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationsException(MethodArgumentNotValidException exception){
        Map<String,String> errors = new HashMap<>();
        
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));
        String errorMesssage = "Errores de validación en los campos: " +String.join(" ,", errors.keySet());
        ErrorResponse errorResponse = new ErrorResponse(
                errorMesssage,
                HttpStatus.BAD_REQUEST.value(),
                "Validacion Fallida");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
}
