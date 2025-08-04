package org.example.person;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.pet.PetService;
import org.example.util.ExitsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.Main.menuStack;

@UtilityClass
@Slf4j
public class PersonService {

    String yourName;
    String yourSecName;
    Scanner console = new Scanner(System.in);
    String input;

    public void processPersonMenu() {
        log.info("1 - ты хочешь вручную ввести свои Ф-И,");
        log.info("2 - создадим персона или несколько");
        log.info("или выйти в прошлое меню через exit");
        input = console.nextLine();
        while (!input.equalsIgnoreCase("exit") && !input.equals("1") && !input.equals("2")) {
            log.warn("1, 2 или exit - не попал, повтори");
            input = console.nextLine();
        }
        switch (input) {
            case "1":
                menuStack.addLast(PersonService::manuallyNameFamilyMenu);
                return;
            case "2":
                menuStack.addLast(PersonService::addPersons);
                return;
            case "exit":
                menuStack.pop();
                return;
            default:
                break;
        }
    }


    private void addPersons() {
        log.info("введи имя:");
        String name = console.nextLine();
        log.info("фамилию:");
        String surname = console.nextLine();
        log.info("возраст");
        Integer age = console.nextInt();
        console.nextLine();
        log.info("сколько таких ты хочешь?");
        int n = console.nextInt();
        console.nextLine();
        if (n == 1) {
            PersonHolder.personHolder.put(name, new Person(name, surname, age, new ArrayList<>()));
            askToAddPets();
            log.info("создан: {}", PersonHolder.personHolder.get(name).toString());
            ExitsUtils.informingBack();
        } else {
            if (n > 0) {
                List<String> tempPersons = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    Person person = new Person(name + "_" + i, surname + "_" + i, age, new ArrayList<>());
                    PersonHolder.personHolder.put(person.getName(), person);
                    tempPersons.add(person.getName());
                }
                askToAddPets();
                log.info("созданы:");
                for (String tempName : tempPersons) {
                    Person tempPerson = PersonHolder.personHolder.get(tempName);
                    log.info("{}", tempPerson);
                }
                ExitsUtils.informingBack();
            } else {
                log.error("не сильно-то и хочешь, возвращаемся назад");
                menuStack.pop();
            }
        }
    }

    private void manuallyNameFamilyMenu() {
        log.info("братищщка, чекни имя: ");
        yourName = console.nextLine();
        log.info("Теперича - как род ваш именуют сударь: ");
        yourSecName = console.nextLine();
        log.info("Тебя зовут - {} {}", yourName, yourSecName);
        ExitsUtils.informingBack();
    }

    private void askToAddPets() {
        log.info("хочешь добавить питомца (ев)?" +
                "1- да," +
                "любой другой ввод - нет");
        input = console.nextLine();
        if (input.equals("1")) {
            PetService.addPets(false);
        }
    }


}
