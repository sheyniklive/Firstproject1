package org.example.pet;

import org.example.person.PersonHolder;

import java.util.Scanner;

public class PetService {
    static Scanner console = new Scanner(System.in);

    public static void processPet() {
        System.out.println("как питомца зовут?");
        String petName = console.nextLine();
        System.out.println("кто это:" +
                "1 - кошка" +
                "2 - собака" +
                "3 - гусь");
        String input = console.nextLine();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
            System.out.println("только 1, 2 или 3");
            input = console.nextLine();
        }

        switch (input) {
            case "1":
                new Cat(petName);
                break;
            case "2":
                new Dog(petName);
                break;
            case "3":
                new Goose(petName);
                break;
        }
        System.out.println("кому из людей ты хочешь пристроить животное?");
        System.out.println(PersonHolder.personHolder.keySet().toString());
        String wantPerson = console.nextLine();
        while (!PersonHolder.personHolder.containsKey(wantPerson)) {
            System.out.println("такого человека не найдено, добавить не получится, введи еще раз как в списке");
            wantPerson = console.nextLine();
        }

    }

}
