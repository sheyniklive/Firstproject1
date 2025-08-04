package org.example.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.example.pet.Pet;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class Person {
    private String name;
    private String surname;
    private Integer age;
    private List<Pet> pets;
}