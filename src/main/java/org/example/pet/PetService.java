package org.example.pet;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidMenuChoiceException;
import org.example.exception.PersonNotFoundException;
import org.example.person.Person;
import org.example.repository.PersonRepository;
import org.example.util.ExitsUtils;
import org.example.validator.Validators;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.example.Main.console;
import static org.example.Main.menuStack;

@Slf4j
public class PetService {
    private String input;
    private String petName;
    private Person currentPerson;

    private final Map<String, Runnable> choiceProcessPetServiceMenu = Map.of(
            "1", this::getPersonPets,
            "2", () -> addPets(true));

    private final Map<String, Runnable> choiceAddPets = Map.of(
            "1", () -> currentPerson.getPets().add(new Cat(petName)),
            "2", () -> currentPerson.getPets().add(new Dog(petName)),
            "3", () -> currentPerson.getPets().add(new Goose(petName)));

    private final Map<String, Runnable> choiceGetPersonPets = Map.of(
            "1", () -> {
                for (Pet pet : currentPerson.getPets()) {
                    log.info("{}({})", pet.getName(), pet.getType());
                }
                ExitsUtils.informingBack();
            },
            "2", () -> {
                for (Pet pet : currentPerson.getPets()) {
                    pet.makeSound();
                }
                ExitsUtils.informingBack();
            });

    private final PersonRepository repo;

    public PetService(PersonRepository personRepository) {
        this.repo = personRepository;
    }

    public void processPetServiceMenu() {
        log.info("выбирай:");
        log.info("1 - пойдем к чьим-то питомцам,");
        log.info("2 - можем добавить кому-нибудь из людей новых");
        log.info("или 'exit' для возврата");
        while (true) {
            try {
                input = console.nextLine().trim();
                Validators.choiceMenuOf2.validate(input);
                break;
            } catch (InvalidMenuChoiceException e) {
                log.error("Ошибка выбора меню в PetService`е", e);
                log.info("повтори, только {} или exit", choiceProcessPetServiceMenu.keySet());
            }
        }
        if (input.equals("exit")) {
            menuStack.removeLast();
            return;
        }
        Runnable next = choiceProcessPetServiceMenu.get(input);
        menuStack.addLast(next);
    }

    public void addPets(boolean needInformingBack) {
        boolean hasAnyDbData = repo.isExistDbData();
        if (!hasAnyDbData) {
            log.warn("пока не добавлено ни одного человека");
            menuStack.removeLast();
            return;
        }
        log.info("кому из людей ты хочешь пристроить животное?");
        whatPersonWant();
        do {
            log.info("поехали: как питомца зовут?");
            petName = console.nextLine().trim();
            log.info("кто это:");
            log.info("1 - кошка");
            log.info("2 - собака");
            log.info("3 - гусь");
            input = console.nextLine().trim();
            while (!choiceAddPets.containsKey(input)) {
                log.warn("только 1, 2 или 3 - повтори");
                input = console.nextLine().trim();
            }
            Runnable addCertainPet = choiceAddPets.get(input);
            addCertainPet.run();
            log.info("питомцы персона '{}' обновлены: {}", currentPerson.getName(), currentPerson.getPets());
            log.info("хочешь добавить нового:");
            log.info("1 - да,");
            log.info("0 - закончим");
            while (true) {
                try {
                    input = console.nextLine().trim();
                    Validators.yesNo.validate(input);
                    break;
                } catch (InvalidMenuChoiceException e) {
                    log.error("Ошибка выбора действия", e);
                    log.info("попробуй еще: 1/0");
                }
            }
        } while (input.equals("1"));
        repo.save(currentPerson);
        if (needInformingBack) {
            ExitsUtils.informingBack();
        }
    }

    private void getPersonPets() {
        boolean hasAnyDbData = repo.isExistDbData();
        if (!hasAnyDbData) {
            log.warn("пока нет ни одного человека");
            menuStack.removeLast();
            return;
        }
        log.info("с чьими животными ты хочешь взаимодействовать?");
        whatPersonWant();
        if (currentPerson.getPets().isEmpty()) {
            log.warn("к сожалению, у этого человека пока нет животных");
            menuStack.removeLast();
            return;
        }
        log.info("вот список его(ее) питомцев: {}", currentPerson.getPets());
        log.info("твой выбор:");
        log.info("1 - получить их кличку и вид");
        log.info("2 - они издадут звук (кто умеет)");
        log.info("'exit' для возврата");
        while (true) {
            try {
                input = console.nextLine().trim();
                Validators.choiceMenuOf2.validate(input);
                break;
            } catch (InvalidMenuChoiceException e) {
                log.error("Ошибка выбора действия с питомцами", e);
                log.info("повтори: {} или exit", choiceGetPersonPets.keySet());
            }
        }
        if (input.equals("exit")) {
            menuStack.removeLast();
            return;
        }
        Runnable actionWithPets = choiceGetPersonPets.get(input);
        actionWithPets.run();
    }

    private void whatPersonWant() {
        List<String> existsPersonsNames = repo.showAllNames();
        while (!existsPersonsNames.isEmpty()) {
            log.info(existsPersonsNames.toString());
            try {
                String wantPerson = console.nextLine().trim();
                if (existsPersonsNames.contains(wantPerson)) {
                    Optional<Person> foundedPerson = repo.findByName(wantPerson);
                    foundedPerson.ifPresent(person -> currentPerson = person);
                    break;
                } else {
                    throw new PersonNotFoundException(wantPerson);
                }
            } catch (PersonNotFoundException e) {
                log.error("Ошибка ввода имени", e);
                log.info("попробуй еще");
            }
        }
    }
}
