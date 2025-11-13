package com.techup.travel.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // รวมศูนย์ดัก error จากทุก Controller
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class) // ดัก @Valid ที่ fail
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> fieldErrors = new HashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      fieldErrors.put(fe.getField(), fe.getDefaultMessage()); // เก็บ {field: message}
    }

    Map<String, Object> body = new HashMap<>();
    body.put("message", "Validation failed");
    body.put("errors", fieldErrors);

    return ResponseEntity.badRequest().body(body); // ส่ง 400 + JSON
  }
}
