package org.example.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(Long id) {
        super(String.format("Pet with id %d not found", id));
    }

}
