package org.example.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pet.enums.PetType;

import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Slf4j
public class Goose implements Pet {
    private Long id;
    private final String name;
    private UUID ownerId;

    @Override
    public PetType getType() {
        return PetType.GOOSE;
    }
}
