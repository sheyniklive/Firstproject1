package org.example.pet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface Pet {
    String getName();

    String getType();

    void makeSound();
}

@RequiredArgsConstructor
@Getter
class Cat implements Pet {
    private final String name;

    @Override
    public String getType() {
        return "Cat";
    }

    @Override
    public void makeSound() {
        System.out.println("Meow");
    }

}

@RequiredArgsConstructor
@Getter
class Dog implements Pet {
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

@RequiredArgsConstructor
@Getter
class Goose implements Pet {
    private final String name;

    @Override
    public String getType() {
        return "Goose";
    }

    public void makeSound() {
        System.out.println("GaGaGa");
    }
}

