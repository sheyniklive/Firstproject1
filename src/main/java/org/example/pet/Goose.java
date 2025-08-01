package org.example.pet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Goose implements InterfacePet{
    private final String name;
    @Override
    public String getType() {
        return "Goose";
    }
    public void makeSound() {
        System.out.println("GaGaGa");
    }
}
