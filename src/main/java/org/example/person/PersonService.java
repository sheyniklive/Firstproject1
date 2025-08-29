package org.example.person;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidAgeException;
import org.example.exception.InvalidMenuChoiceException;
import org.example.util.ExitsUtils;
import org.example.validator.Validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.Main.*;


@Slf4j
public class PersonService {
    private String input;

    private final Map<String, Runnable> choicePersonMenu = Map.of(
            "1", this::manuallyNameFamilyMenu,
            "2", this::addPersons);

    public void processPersonMenu() {
        log.info("1 - ты хочешь вручную ввести свои Ф-И,");
        log.info("2 - создадим персона или несколько");
        log.info("или выйти в прошлое меню через exit");
        while (true) {
            try {
                input = console.nextLine().trim();
                Validators.choiceMenuOf2.validate(input);
                break;
            } catch (InvalidMenuChoiceException e) {
                log.error("Ошибка выбора меню в PersonService`е", e);
                log.info("Попробуй еще раз: {} или exit", choicePersonMenu.keySet());
            }
        }
        if (input.equalsIgnoreCase("exit")) {
            menuStack.removeLast();
            return;
        }
        Runnable next = choicePersonMenu.get(input);
        menuStack.addLast(next);
    }


    private void addPersons() {
        log.info("введи имя:");
        String name = console.nextLine().trim();
        log.info("фамилию:");
        String surname = console.nextLine().trim();
        log.info("возраст");
        while (true) {
            try {
                input = console.nextLine().trim();
                Validators.isValidAge.validate(input);
                break;
            } catch (InvalidAgeException e) {
                log.error("Ошибка при вводе возраста", e);
                log.info("давай еще раз: должно быть от 0 до 150 лет");
            }
        }
        Integer age = Integer.parseInt(input);
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
                    log.info("{}", tempPerson.toString());
                }
                ExitsUtils.informingBack();
            } else {
                log.error("введено недопустимое число персонов - 0");
                log.info("не сильно-то и хочешь, возвращаемся назад");
                menuStack.removeLast();
            }
        }
    }

    private void manuallyNameFamilyMenu() {
        log.info("братищщка, чекни имя: ");
        String yourName = console.nextLine().trim();
        log.info("Теперича - как род ваш именуют сударь: ");
        String yourSecName = console.nextLine().trim();
        log.info("Тебя зовут - {} {}", yourName, yourSecName);
        ExitsUtils.informingBack();
    }

    private void askToAddPets() {
        log.info("хочешь добавить питомца (ев)?");
        log.info("1- да,");
        log.info("0 - нет");
        while (true) {
            try {
                input = console.nextLine().trim();
                Validators.yesNo.validate(input);
                break;
            } catch (InvalidMenuChoiceException e) {
                log.error("Ошибка выбора действия", e);
                log.info("введи еще раз 1 или 0");
            }
        }
        if (input.equals("1")) {
            do {
                petService.addPets(false);
                log.info("хочешь добавить другому?");
                log.info("1 - да");
                log.info("0 - продолжим");
                while (true) {
                    try {
                        input = console.nextLine().trim();
                        Validators.yesNo.validate(input);
                        break;
                    } catch (InvalidMenuChoiceException e) {
                        log.error("Ошибка выбора действия", e);
                        log.info("1 или 0, еще раз пож-та");
                    }
                }
            } while (input.equals("1"));
        }
    }
}
