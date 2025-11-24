package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePersonNotFoundException(PersonNotFoundException personNFE) {
        log.warn("Персон не найден", personNFE);
        return buildErrorResponse(HttpStatus.NOT_FOUND, personNFE.getMessage());
    }

    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePetNotFoundException(PetNotFoundException petNFE) {
        log.warn("Питомец не найден", petNFE);
        return buildErrorResponse(HttpStatus.NOT_FOUND, petNFE.getMessage());
    }

    @ExceptionHandler(InvalidOwnershipException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidOwnershipException(InvalidOwnershipException invalidOwnershipException) {
        log.warn("Питомец не принадлежит персону", invalidOwnershipException);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, invalidOwnershipException.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        log.warn("Передано недопустимое значение", illegalArgumentException);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, illegalArgumentException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception e) {
        log.error("Ошибка сервера", e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
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
