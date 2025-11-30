package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pet.enums.PetType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PetCreateDto {
    private String name;
    private PetType type;
}
