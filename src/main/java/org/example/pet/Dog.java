package org.example.pet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Dog implements InterfacePet {
    private final String name;

    @Override
    public String getType() {
        return "Dog";
    }

    @Override
    public void makeSound() {
        System.out.println("Wow");
    }
}
