package org.example.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.pet.Pet;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Person {
    private String name;
    private String surname;
    private Integer age;
    private List<Pet> pets;
}
