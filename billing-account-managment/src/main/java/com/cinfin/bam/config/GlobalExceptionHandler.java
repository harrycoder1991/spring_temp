package com.cinfin.bam.config;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  private Map<String, Object> createErrorResponse(MethodArgumentNotValidException ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("code", "VALIDATION_ERROR");
    error.put("message", "Validation failed");
    error.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
    error.put("details", getValidationErrors(ex));
    return error;
  }

  private Map<String, String> getValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName;
      if (error instanceof FieldError) {
        fieldName = ((FieldError) error).getField();
      } else {
        fieldName = error.getObjectName();
      }
      errors.put(fieldName, error.getDefaultMessage());
    });
    return errors;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", "error");
    errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("error", createErrorResponse(ex));
    return ResponseEntity.badRequest().body(errorResponse);
  }
}
