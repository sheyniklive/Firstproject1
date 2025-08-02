package org.example.person;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.pet.PetService;
import org.example.util.ExitsUtils;

import java.util.ArrayList;
import java.util.Scanner;

@UtilityClass
@Slf4j
public class PersonService {

    String yourName;
    String yourSecName;
    Scanner console = new Scanner(System.in);
    String input;

    public void processPerson() {
        System.out.println("1 - ты хочешь вручную ввести свои Ф-И,");
        System.out.println("2 - создадим персона или несколько");
        System.out.println("или выйти через exit");
        input = console.nextLine();
        switch (input) {
            case "1":
                System.out.println("братищщка, чекни имя: ");
                yourName = console.nextLine();
                System.out.println("Теперича - как род ваш именуют сударь: ");
                yourSecName = console.nextLine();
                System.out.println("Тебя зовут - " + yourName + " " + yourSecName);
                break;
            case "2":
                System.out.println("1 - создать персона(ов) или exit - для котопультирования из программы");
                switch (input) {
                    case "1":
                        addPersons();
                        break;
                    case "exit":
                        ExitsUtils.doExit();
                        break;
                    default:
                        System.out.println("надо было 1, 2 или exit - перезапусти прорамму");
                        break;
                }
                break;
            case "exit":
                ExitsUtils.doExit();
                break;
            default:
                System.out.println("1, 2 or exit - не попал, выходи и перезапусти");
                ExitsUtils.doExit();
                break;
        }
    }


    private void addPersons() {
        System.out.println("введи имя:");
        String name = console.nextLine();
        System.out.println("фамилию:");
        String surname = console.nextLine();
        System.out.println("возраст");
        Integer age = console.nextInt();
        console.nextLine();
        System.out.println("сколько таких ты хочешь?");
        Integer n = console.nextInt();
        console.nextLine();
        if (n == 1) {
            PersonHolder.personHolder.put(name, new Person(name, surname, age, new ArrayList<>()));
            askAddPets();
            System.out.println(PersonHolder.personHolder.get(name).toString());
        } else {
            if (n > 0) {

                for (Integer i = 0; i < n; i++) {
                    PersonHolder.personHolder.put(name + "_" + i, new Person(name + "_" + i, surname + "_" + i, age, new ArrayList<>()));
                }
                askAddPets();
            } else {
                System.out.println("не сильно-то и хочешь, пока");
                ExitsUtils.doExit();
            }
        }
    }

    private void askAddPets() {
        System.out.println("хочешь добавить питомца (ев)?" +
                "1- да," +
                "0- нет");
        input = console.nextLine();
        switch (input) {
            case "1":
                PetService.addPets();
                break;
            case "0":
                break;
        }
    }
}
