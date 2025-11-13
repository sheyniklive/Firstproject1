package org.example.pet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.example.pet.enums.PetType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cat.class, name = "cat"),
        @JsonSubTypes.Type(value = Dog.class, name = "dog"),
        @JsonSubTypes.Type(value = Goose.class, name = "goose")
})
public interface Pet {
    String getName();

    @JsonIgnore
    PetType getType();

    void makeSound();
}

@Getter
@Slf4j
@ToString
class Cat implements Pet {
    private final String name;

    @JsonCreator
    public Cat(@JsonProperty("name") String name) {
        this.name = name;
    }

    @Override
    public PetType getType() {
        return PetType.CAT;
    }

    @Override
    public void makeSound() {
        log.info("{}({}) -> Meow", getName(), getType().getTypeNameForUI());
    }
}

@Getter
@Slf4j
@ToString
class Dog implements Pet {
    private final String name;

    @JsonCreator
    public Dog(@JsonProperty("name") String name) {
        this.name = name;
    }

    @Override
    public PetType getType() {
        return PetType.DOG;
    }

    @Override
    public void makeSound() {
        log.info("{}({}) -> Wow", getName(), getType().getTypeNameForUI());
    }
}

@Getter
@Slf4j
@ToString
class Goose implements Pet {
    private final String name;

    @JsonCreator
    public Goose(@JsonProperty("name") String name) {
        this.name = name;
    }

    @Override
    public PetType getType() {
        return PetType.GOOSE;
    }

    public void makeSound() {
        log.info("{}({}) -> GaGaGa", getName(), getType().getTypeNameForUI());
    }
}

