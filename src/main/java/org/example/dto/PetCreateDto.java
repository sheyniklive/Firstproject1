package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.pet.enums.PetType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PetCreateDto {
    private String name;
    private PetType type;
}
