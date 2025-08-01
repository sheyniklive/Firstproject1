package org.example.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.pet.Pet;

import java.util.List;

@Data
@AllArgsConstructor
public class Person {
    private String name;
    private final String surname;
    private final Integer age;
    List<Pet> pets;
}
