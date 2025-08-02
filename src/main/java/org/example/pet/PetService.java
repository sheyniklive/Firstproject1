package org.example.pet;

import org.example.person.Person;
import org.example.person.PersonHolder;

import java.util.Scanner;

public class PetService {
    static Scanner console = new Scanner(System.in);
    static String input;

    public static void addPets() {
        System.out.println("кому из людей ты хочешь пристроить животное?");
        System.out.println(PersonHolder.personHolder.keySet().toString());
        String wantPerson = console.nextLine();
        while (!PersonHolder.personHolder.containsKey(wantPerson)) {
            System.out.println("такого человека не найдено, добавить не получится, введи еще раз как в списке");
            wantPerson = console.nextLine();
        }
        do {
            System.out.println("поехали: как питомца зовут?");
            String petName = console.nextLine();
            System.out.println("кто это:" +
                    "1 - кошка" +
                    "2 - собака" +
                    "3 - гусь");
            input = console.nextLine();
            while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                System.out.println("только 1, 2 или 3");
                input = console.nextLine();
            }
            switch (input) {
                case "1":
                    PersonHolder.personHolder.get(wantPerson).getPets().add(new Cat(petName));
                    break;
                case "2":
                    PersonHolder.personHolder.get(wantPerson).getPets().add(new Dog(petName));
                    break;
                case "3":
                    PersonHolder.personHolder.get(wantPerson).getPets().add(new Goose(petName));
                    break;
            }
            System.out.println("хочешь добавить нового:");
            System.out.println("пиши 'еще',");
            System.out.println("любой другой ввод - закончим");
            input = console.nextLine();
        } while (input.equals("еще"));
    }
}
