package br.com.francisco.TodoList.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpException(HttpMessageNotReadableException e){
        return ResponseEntity.status(401).body(e.getMostSpecificCause().getMessage());
    }
    
}
