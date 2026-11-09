package com.sysrefugios.backend.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(RecursoNoEncontradoException excepcion) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo(excepcion.getMessage()));
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, Object>> manejarCredencialesInvalidas(CredencialesInvalidasException excepcion) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cuerpo(excepcion.getMessage()));
    }

    @ExceptionHandler(NoAutenticadoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoAutenticado(NoAutenticadoException excepcion) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cuerpo(excepcion.getMessage()));
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Map<String, Object>> manejarAccesoDenegado(AccesoDenegadoException excepcion) {
        Map<String, Object> cuerpoRespuesta = cuerpo(excepcion.getMessage());
        cuerpoRespuesta.put("codigo", excepcion.getCodigo());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(cuerpoRespuesta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarArgumentoInvalido(IllegalArgumentException excepcion) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo(excepcion.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarCuerpoInvalido(HttpMessageNotReadableException excepcion) {
        String mensaje = "El cuerpo del pedido no es válido";
        if (excepcion.getCause() instanceof InvalidFormatException formato && !formato.getPath().isEmpty()) {
            String campo = formato.getPath().get(formato.getPath().size() - 1).getFieldName();
            mensaje = "Valor inválido para el campo " + campo;
            Class<?> tipo = formato.getTargetType();
            if (tipo != null && tipo.isEnum()) {
                mensaje += ". Valores aceptados: " + Arrays.toString(tipo.getEnumConstants());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo(mensaje));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException excepcion) {
        Map<String, String> errores = new HashMap<>();
        excepcion.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> cuerpoRespuesta = cuerpo("Error de validación");
        cuerpoRespuesta.put("errores", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpoRespuesta);
    }

    private Map<String, Object> cuerpo(String mensaje) {
        Map<String, Object> cuerpoRespuesta = new HashMap<>();
        cuerpoRespuesta.put("timestamp", LocalDateTime.now());
        cuerpoRespuesta.put("mensaje", mensaje);
        return cuerpoRespuesta;
    }
}
