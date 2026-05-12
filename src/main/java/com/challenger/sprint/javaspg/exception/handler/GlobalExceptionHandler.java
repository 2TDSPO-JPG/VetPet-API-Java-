package com.challenger.sprint.javaspg.exception.handler;

import com.challenger.sprint.javaspg.exception.execptions.DadoExistenteException;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;
import com.challenger.sprint.javaspg.exception.execptions.LoginException;
import com.challenger.sprint.javaspg.exception.execptions.SenhaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DadoExistenteException.class)
    public ResponseEntity<String> emailExisteException(
            DadoExistenteException ex
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> loginException(LoginException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EntidadeNaoPersistidaException.class)
    public ResponseEntity<String> entidadeNaoPersistidaException(EntidadeNaoPersistidaException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SenhaException.class)
    public ResponseEntity<String> senhaException(SenhaException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
