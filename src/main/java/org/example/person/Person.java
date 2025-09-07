package org.example.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pet.Pet;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private List<Pet> pets;

    Person(String name, String surname, Integer age, List<Pet> pets) {
        this(null, name, surname, age, pets);
    }
}