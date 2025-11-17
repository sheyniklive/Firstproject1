package org.example.pet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

    void setOwnerId(UUID id);

    void makeSound();
}

@Getter
@Setter
@Slf4j
@ToString
class Cat implements Pet {
    private Long id;
    private String name;
    private UUID ownerId;

    @JsonCreator
    public Cat(@JsonProperty("name") String name) {
        this.name = name;
    }

    public Cat(Long id, String name, UUID ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
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
@Setter
@Slf4j
@ToString
class Dog implements Pet {
    private Long id;
    private String name;
    private UUID ownerId;

    @JsonCreator
    public Dog(@JsonProperty("name") String name) {
        this.name = name;
    }

    public Dog(Long id, String name,UUID ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
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
@Setter
@Slf4j
@ToString
class Goose implements Pet {
    private Long id;
    private String name;
    private UUID ownerId;

    @JsonCreator
    public Goose(@JsonProperty("name") String name) {
        this.name = name;
    }

    public Goose(Long id, String name, UUID ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    @Override
    public PetType getType() {
        return PetType.GOOSE;
    }

    public void makeSound() {
        log.info("{}({}) -> GaGaGa", getName(), getType().getTypeNameForUI());
    }
}

