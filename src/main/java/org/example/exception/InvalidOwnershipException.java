package org.example.exception;

import java.util.UUID;

public class InvalidOwnershipException extends RuntimeException {
    public InvalidOwnershipException(Long petId, UUID personId) {
        super(String.format("Pet with id {%s} does not belong to person with id {%s}", petId, personId));
    }
}
