package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PersonResponseDto {
    private UUID id;
    private String name;
    private String surname;
    private Integer age;
    private List<PetDto> pets;
}
