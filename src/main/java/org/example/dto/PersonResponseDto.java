package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonResponseDto {
    private UUID id;
    private String name;
    private String surname;
    private Integer age;
}
