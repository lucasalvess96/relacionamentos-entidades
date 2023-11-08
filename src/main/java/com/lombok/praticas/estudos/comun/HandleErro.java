package com.lombok.praticas.estudos.comun;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class HandleErro {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroMessage> handleMethodValidation(MethodArgumentNotValidException notValidException,
                                                              HttpServletRequest httpServletRequest) {
        ErroMessage erroMessage = new ErroMessage();
        erroMessage.setTimestamp(LocalDateTime.now());
        erroMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        erroMessage.setError("Ocorreu erro ao salvar informações, verifique seus campos de preenchimentos");
        erroMessage.setPath(httpServletRequest.getRequestURI());
        erroMessage.setMessage(notValidException.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> err.unwrap(ConstraintViolation.class))
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", ")));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(erroMessage);
    }

    @ExceptionHandler(ErroRequest.class)
    public ResponseEntity<ErroMessage> entityNotFoundException(ErroRequest errorRequest,
                                                               HttpServletRequest httpServletRequest) {
        ErroMessage erroMessage = new ErroMessage();
        erroMessage.setTimestamp(LocalDateTime.now());
        erroMessage.setStatus(HttpStatus.NOT_FOUND.value());
        erroMessage.setError("Recurso não encontrado");
        erroMessage.setPath(httpServletRequest.getRequestURI());
        if (errorRequest.getMessage() != null && !errorRequest.getMessage()
                .isEmpty()) {
            erroMessage.setMessage(errorRequest.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(erroMessage);
    }
}
