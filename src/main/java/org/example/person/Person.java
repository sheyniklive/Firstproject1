package org.example.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pet.Pet;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private UUID id;
    private String name;
    private String surname;
    private int age;
    private List<Pet> pets;
}