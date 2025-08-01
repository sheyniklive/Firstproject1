package org.example.pet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Cat implements InterfacePet {
    private final String name;

    @Override
    public String getType() {
        return "cat";
    }

    @Override
    public void makeSound() {
        System.out.println("meow");
    }

}
