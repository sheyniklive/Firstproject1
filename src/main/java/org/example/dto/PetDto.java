package org.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.pet.Pet;

@NoArgsConstructor
@Getter
public class PetDto {
    private String name;
    private String type;

    public PetDto(Pet pet) {
        this.name = pet.getName();
        this.type = pet.getType();
    }
}
