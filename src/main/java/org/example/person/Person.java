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

    public Person(String name, String surname, Integer age, List<Pet> pets) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.pets = pets;
    }
    public Person() {
        this.id = UUID.randomUUID();
    }
}