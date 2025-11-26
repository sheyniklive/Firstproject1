package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonNotFoundException(PersonNotFoundException personNFE) {
        log.warn("Персон не найден", personNFE);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, personNFE.getMessage()));
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePetNotFoundException(PetNotFoundException petNFE) {
        log.warn("Питомец не найден", petNFE);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(HttpStatus.NOT_FOUND, petNFE.getMessage()));
    }

    @ExceptionHandler(InvalidOwnershipException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOwnershipException(InvalidOwnershipException invalidOwnershipException) {
        log.warn("Питомец не принадлежит персону", invalidOwnershipException);
        return ResponseEntity.badRequest().
                body(buildErrorResponse(HttpStatus.BAD_REQUEST, invalidOwnershipException.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        log.warn("Передано недопустимое значение", illegalArgumentException);
        return ResponseEntity.badRequest()
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, illegalArgumentException.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        log.error("Ошибка сервера", e);
        return ResponseEntity.internalServerError()
                .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера"));
    }
    private ErrorResponse buildErrorResponse(HttpStatus httpStatus, String  message) {
        return new ErrorResponse(
                Instant.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message
        );
    }
}
