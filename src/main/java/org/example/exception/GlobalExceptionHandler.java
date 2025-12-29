package org.example.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonNotFoundException(PersonNotFoundException personNFE) {
        log.warn("Персон не найден", personNFE);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, personNFE.getMessage(), null));
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePetNotFoundException(PetNotFoundException petNFE) {
        log.warn("Питомец не найден", petNFE);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, petNFE.getMessage(), null));
    }

    @ExceptionHandler(InvalidOwnershipException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOwnershipException(InvalidOwnershipException invalidOwnershipException) {
        log.warn("Питомец не принадлежит персону", invalidOwnershipException);
        return ResponseEntity.badRequest().
                body(buildErrorResponse(HttpStatus.BAD_REQUEST, invalidOwnershipException.getMessage(), null));
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyNotFoundException(CurrencyNotFoundException currencyNFE) {
        log.warn("Валюта не найдена", currencyNFE);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, currencyNFE.getMessage(), null));
    }

    @ExceptionHandler(CbrApiException.class)
    public ResponseEntity<ErrorResponse> handleCbrApiException(CbrApiException cbrApiException) {
        log.error("Ошибка при работе с CbrApi", cbrApiException);
        return ResponseEntity.internalServerError()
                .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, cbrApiException.getMessage(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        log.warn("Передано недопустимое значение", illegalArgumentException);
        return ResponseEntity.badRequest()
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, illegalArgumentException.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {

        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors()
                .forEach(fieldError ->
                        errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        log.warn("Ошибка валидации тела запроса. Ошибки: {}", errors);

        return ResponseEntity.badRequest()
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, "Ошибка валидации входных данных", errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {

        Map<String, String> errors = new HashMap<>();
        constraintViolationException.getConstraintViolations()
                .forEach(constraintViolation -> {
                    String propertyPath = constraintViolation.getPropertyPath().toString();
                    String message = constraintViolation.getMessage();

                    String parameterName = propertyPath.contains(".")
                            ? propertyPath.substring(propertyPath.lastIndexOf('.') + 1)
                            : propertyPath;
                    errors.put(parameterName, message);
                });

        log.warn("Ошибка валидации путевых параметров. Ошибки: {}", errors);

        return ResponseEntity.badRequest()
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, "Ошибка валидации параметров запроса", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        log.error("Ошибка сервера", e);
        return ResponseEntity.internalServerError()
                .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", null));
    }

    private ErrorResponse buildErrorResponse(HttpStatus httpStatus, String message, Map<String, String> errors) {
        return new ErrorResponse(
                Instant.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                errors
        );
    }
}
