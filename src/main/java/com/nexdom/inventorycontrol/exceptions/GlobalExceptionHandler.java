package com.nexdom.inventorycontrol.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorRecordResponse> handleNotFoundException(NotFoundException ex) {
        var errorRecordResponse = new ErrorRecordResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
        logger.error("NotFoundException message {}", errorRecordResponse);
        return new ResponseEntity<>(errorRecordResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessInsuficientStock.class)
    public ResponseEntity<ErrorRecordResponse> handleBusinessInsuficientStockException(BusinessInsuficientStock ex) {
        var errorRecordResponse = new ErrorRecordResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        logger.error("BusinessInsuficientStock message {}", errorRecordResponse);
        return new ResponseEntity<>(errorRecordResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRecordResponse> handleIllegalArgumentExceptionException(IllegalArgumentException ex) {
        var errorRecordResponse = new ErrorRecordResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        logger.error("IllegalArgumentException message {}", errorRecordResponse);
        return new ResponseEntity<>(errorRecordResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRecordResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fielName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fielName, errorMessage);
            }
        );
        var errorRecordResponse = new ErrorRecordResponse(HttpStatus.BAD_REQUEST.value(), "Erro: validação falhou", errors);
        logger.error("MethodArgumentNotValidException message {}", errorRecordResponse);
        return new ResponseEntity<>(errorRecordResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorRecordResponse> handleOptimisticException(ObjectOptimisticLockingFailureException ex) {
        var errorRecordResponse = new ErrorRecordResponse(HttpStatus.CONFLICT.value(), "Produto esta sendo atualizado por outro usuario, tente novamente mais tarde", null);
        logger.error("ObjectOptimisticLockingFailureException message {}", errorRecordResponse);
        return new ResponseEntity<>(errorRecordResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductMovementStockExist.class)
    public ResponseEntity<ErrorRecordResponse> productByExistInStockMovement(ProductMovementStockExist ex) {
        var errorRecordResponse = new ErrorRecordResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        logger.error("ProductMovementStockExist message {}", errorRecordResponse);
        return new ResponseEntity<>(errorRecordResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRecordResponse> handleJsonParseErrors(HttpMessageNotReadableException ex) {

        Map<String, String> errors = new HashMap<>();
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ifx) {
            // nome do campo que falhou (último elemento do path JSON)
            String fieldName = ifx.getPath().isEmpty()
                    ? "request"
                    : ifx.getPath().get(ifx.getPath().size() - 1).getFieldName();

            Class<?> targetType = ifx.getTargetType();

            /* ---------- ENUM inválido ---------- */
            if (targetType.isEnum()) {
                errors.put(fieldName,
                        "Invalid value '" + ifx.getValue() + "' for enum "
                                + targetType.getSimpleName());
                return new ResponseEntity<>(
                        new ErrorRecordResponse(HttpStatus.BAD_REQUEST.value(),
                                "Erro: valor de enum invalido", errors),
                        HttpStatus.BAD_REQUEST);
            }

            /* ---------- UUID inválido ---------- */
            if (UUID.class.isAssignableFrom(targetType)) {
                errors.put(fieldName,
                        "Invalid UUID format. Expected 36‑character UUID.");
                return new ResponseEntity<>(
                        new ErrorRecordResponse(HttpStatus.BAD_REQUEST.value(),
                                "Erro: UUID valor invalido", errors),
                        HttpStatus.BAD_REQUEST);
            }
        }

        // fallback genérico (qualquer outro problema de leitura do JSON)
        var errorRecordResponse = new ErrorRecordResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON request",
                null);
        return new ResponseEntity<>(errorRecordResponse, HttpStatus.BAD_REQUEST);
    }
}
