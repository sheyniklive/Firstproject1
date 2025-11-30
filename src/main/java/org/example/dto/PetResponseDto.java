package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pet.enums.PetType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PetResponseDto {
    private Long id;
    private String name;
    private PetType type;
    private UUID ownerId;
}
