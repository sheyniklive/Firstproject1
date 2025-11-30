package org.example.pet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.pet.enums.PetType;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cat.class, name = "cat"),
        @JsonSubTypes.Type(value = Dog.class, name = "dog"),
        @JsonSubTypes.Type(value = Goose.class, name = "goose")
})

public interface Pet {

    Long getId();

    String getName();

    @JsonIgnore
    PetType getType();

    UUID getOwnerId();

    void makeSound();
}

@AllArgsConstructor
@Data
@Slf4j
class Cat implements Pet {
    private Long id;
    private final String name;
    private UUID ownerId;

    @Override
    public PetType getType() {
        return PetType.CAT;
    }

    @Override
    public void makeSound() {
        log.info("{}({}) -> Meow", getName(), getType().getTypeNameForUI());
    }
}

@AllArgsConstructor
@Data
@Slf4j
class Dog implements Pet {
    private Long id;
    private final String name;
    private UUID ownerId;

    @Override
    public PetType getType() {
        return PetType.DOG;
    }

    @Override
    public void makeSound() {
        log.info("{}({}) -> Wow", getName(), getType().getTypeNameForUI());
    }
}

@AllArgsConstructor
@Data
@Slf4j
class Goose implements Pet {
    private Long id;
    private final String name;
    private UUID ownerId;

    @Override
    public PetType getType() {
        return PetType.GOOSE;
    }

    public void makeSound() {
        log.info("{}({}) -> GaGaGa", getName(), getType().getTypeNameForUI());
    }
}

