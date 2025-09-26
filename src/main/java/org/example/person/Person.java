package org.example.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.pet.Pet;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Person {
    private UUID id;
    private String name;
    private String surname;
    private Integer age;
    private List<Pet> pets;
}