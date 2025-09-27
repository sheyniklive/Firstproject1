package org.example.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidAgeException;
import org.example.exception.InvalidMenuChoiceException;
import org.example.repository.PersonRepository;
import org.example.util.ExitsUtils;
import org.example.validator.Validators;

import java.util.*;

import static org.example.Main.*;


@Slf4j
@RequiredArgsConstructor
public class PersonService {
    private String input;

    private final Map<String, Runnable> choicePersonMenu = Map.of(
            "1", this::manuallyNameFamilyMenu,
            "2", this::addPersons);

    private final PersonRepository repo;

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
            Person person = new Person(UUID.randomUUID(), name, surname, age, new ArrayList<>());
            repo.save(person);
            askToAddPets();
            log.info("персон создан и загружен в БД: {}", repo.findById((person.getId())).toString());
            ExitsUtils.informingBack();
        } else {
            if (n > 0) {
                List<UUID> tempPersonsId = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    Person person = new Person(UUID.randomUUID(), name + "_" + i, surname + "_" + i, age, new ArrayList<>());
                    repo.save(person);
                    tempPersonsId.add(person.getId());
                }
                askToAddPets();
                log.info("созданы и загружены в БД:");
                for (UUID tempId : tempPersonsId) {
                    Optional<Person> tempPerson = repo.findById(tempId);
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
