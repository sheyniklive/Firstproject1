package org.example.pet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cat.class, name = "cat"),
        @JsonSubTypes.Type(value = Dog.class, name = "dog"),
        @JsonSubTypes.Type(value = Goose.class, name = "goose")
})
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
        log.info("{}({}) -> Meow", getName(), getType());
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
        log.info("{}({}) -> Wow", getName(), getType());
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
        log.info("{}({}) -> GaGaGa", getName(), getType());
    }
}

