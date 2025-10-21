package org.example.pet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidMenuChoiceException;
import org.example.exception.PersonNotFoundException;
import org.example.person.Person;
import org.example.pet.enums.PetType;
import org.example.repository.PersonRepository;
import org.example.util.ExitsUtils;
import org.example.validator.Validators;

import java.util.Map;
import java.util.UUID;

import static org.example.Main.console;
import static org.example.Main.menuStack;

@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final Map<String, Runnable> choiceProcessPetServiceMenu = Map.of(
            "1", this::getPersonPets,
            "2", () -> addPets(true));

    private final PersonRepository repo;

    public void processPetServiceMenu() {
        log.info("выбирай:");
        log.info("1 - пойдем к чьим-то питомцам,");
        log.info("2 - можем добавить кому-нибудь из людей новых");
        log.info("или 'exit' для возврата");
        String input;
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
        if (!repo.isExistDbData()) {
            log.warn("пока не добавлено ни одного человека");
            menuStack.removeLast();
            return;
        }
        Person currentPerson = selectPerson();
        String petName;
        String choisePetType;
        String input;
        do {
            log.info("поехали: как питомца зовут?");
            petName = console.nextLine().trim();
            log.info("кто это:");
            log.info("1 - кошка");
            log.info("2 - собака");
            log.info("3 - гусь");
            while (true) {
                choisePetType = console.nextLine().trim();
                try {
                    Validators.choiceMenuOf3.validate(choisePetType);
                    break;
                } catch (InvalidMenuChoiceException e) {
                    log.warn("только 1, 2 или 3 - повтори");
                }
            }
            addCertainPet(currentPerson, petName, choisePetType);
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
        }
        while (input.equals("1"));
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
        Person currentPerson = selectPerson();
        if (currentPerson.getPets().isEmpty()) {
            log.warn("к сожалению, у этого человека пока нет животных, возвращаемся");
            menuStack.removeLast();
            return;
        }
        log.info("вот список его(ее) питомцев: {}", currentPerson.getPets());
        log.info("твой выбор:");
        log.info("1 - получить их кличку и вид");
        log.info("2 - они издадут звук (кто умеет)");
        log.info("'exit' для возврата");
        String input;
        while (true) {
            try {
                input = console.nextLine().trim();
                Validators.choiceMenuOf2.validate(input);
                break;
            } catch (InvalidMenuChoiceException e) {
                log.error("Ошибка выбора действия с питомцами", e);
                log.info("повтори: 1, 2 или exit");
            }
        }
        if (input.equals("exit")) {
            menuStack.removeLast();
            return;
        }
        actionWithPets(currentPerson, input);
    }

    private Person whatPersonWant() {
        Map<String, String> existsPersonsNamesAndId = repo.showAllNames();

        if (existsPersonsNamesAndId.isEmpty()) {
            throw new PersonNotFoundException();
        }
        while (true) {
            log.info(existsPersonsNamesAndId.keySet().toString());
            String wantPerson = console.nextLine().trim();
            try {
                if (existsPersonsNamesAndId.containsKey(wantPerson)) {
                    return repo.findById(UUID.fromString(existsPersonsNamesAndId.get(wantPerson)))
                            .orElseThrow(() -> new PersonNotFoundException(UUID.fromString(existsPersonsNamesAndId.get(wantPerson))));
                } else {
                    throw new PersonNotFoundException(UUID.fromString(existsPersonsNamesAndId.get(wantPerson)));
                }
            } catch (PersonNotFoundException e) {
                log.error("Ошибка при поиске и загрузке персона", e);
                log.info("Попробуй еще");
            }
        }
    }

    private void addCertainPet(Person person, String petName, String typeChoice) {
        PetType type = PetType.fromMenuChoice(typeChoice);
        switch (type) {
            case CAT -> person.getPets().add(new Cat(petName));
            case DOG -> person.getPets().add(new Dog(petName));
            case GOOSE -> person.getPets().add(new Goose(petName));
            default -> log.warn("неизвестный тип питомца");
        }
    }

    private void actionWithPets(Person person, String action) {
        switch (action) {
            case "1" -> {
                for (Pet pet : person.getPets()) {
                    log.info("{}({})", pet.getName(), pet.getType());
                }
                ExitsUtils.informingBack();
            }
            case "2" -> {
                for (Pet pet : person.getPets()) {
                    pet.makeSound();
                }
                ExitsUtils.informingBack();
            }
            default -> log.warn("неизвестное действие, но его не будет");
        }
    }

    private Person selectPerson() {
        while (true) {
            log.info("назови хозяина (питомцев, дурашка ;) )?");
            try {
                return whatPersonWant();
            } catch (PersonNotFoundException e) {
                log.warn("Ошибка при поиске персона и доступе к нему", e);
                log.info("повтори");
            }
        }


    }
}
