package org.example.pet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

public interface Pet {
    String getName();

    String getType();

    void makeSound();
}

@RequiredArgsConstructor
@Getter
@Slf4j
@ToString
class Cat implements Pet {
    private final String name;

    @Override
    public String getType() {
        return "Cat";
    }

    @Override
    public void makeSound() {
        log.info("Meow");
    }

}

@RequiredArgsConstructor
@Getter
@Slf4j
@ToString
class Dog implements Pet {
    private final String name;

    @Override
    public String getType() {
        return "Dog";
    }

    @Override
    public void makeSound() {
        log.info("Wow");
    }
}

@RequiredArgsConstructor
@Getter
@Slf4j
@ToString
class Goose implements Pet {
    private final String name;

    @Override
    public String getType() {
        return "Goose";
    }

    public void makeSound() {
        log.info("GaGaGa");
    }
}

