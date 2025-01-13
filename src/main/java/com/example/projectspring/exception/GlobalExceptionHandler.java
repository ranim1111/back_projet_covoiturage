package com.example.projectspring.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;


@Component("exceptionHandlerResponse") // Exemple de nom unique
public class GlobalExceptionHandler {


  // Gestion de l'exception UserNotFoundException
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }


  // Gestion d'autres exceptions génériques
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne est survenue.");
  }
}
