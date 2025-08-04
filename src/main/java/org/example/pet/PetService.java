package org.example.pet;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.person.PersonHolder;
import org.example.util.ExitsUtils;

import java.util.Scanner;

import static org.example.Main.menuStack;

@UtilityClass
@Slf4j
public class PetService {
    Scanner console = new Scanner(System.in);
    String input;
    String wantPerson;

    public void processPetServiceMenu() {
        log.info("выбирай:" +
                "1 - пойдем к чьим-то питомцам," +
                "2 - можем добавить кому-нибудь из людей новых" +
                "или 'exit' для возврата");
        input = console.nextLine();
        while (!input.equals("exit") && !input.equals("1") && !input.equals("2")) {
            log.warn("только 1, 2 или 'exit'- повтори");
            input = console.nextLine();
        }
        switch (input) {
            case "1":
                menuStack.addLast(PetService::getPets);
                return;
            case "2":
                menuStack.addLast(PetService::addPets);
                return;
            case "exit":
                menuStack.pop();
                break;
            default:
                break;
        }
    }

    public void addPets() {
        log.info("кому из людей ты хочешь пристроить животное?");
        whatPersonWant();
        do {
            log.info("поехали: как питомца зовут?");
            String petName = console.nextLine();
            log.info("кто это:" +
                    "1 - кошка" +
                    "2 - собака" +
                    "3 - гусь");
            input = console.nextLine();
            while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                log.warn("только 1, 2 или 3 - повтори");
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
                default:
                    break;
            }
            log.info("хочешь добавить нового:");
            log.info("пиши 'еще',");
            log.info("любой другой ввод - закончим");
            input = console.nextLine();
        } while (input.equals("еще"));
    }

    private void getPets() {
        log.info("с чьими животными ты хочешь взаимодействовать?");
        whatPersonWant();
        log.info("вот список его(ее) питомцев: {}", PersonHolder.personHolder.get(wantPerson).getPets().toString());
        log.info("твой выбор:" +
                "1 - получить их кличку и вид" +
                "2 - они издадут звук (кто умеет)" +
                "'exit' для возврата");
        input = console.nextLine();
        while (!input.equals("1") && !input.equals("2") && !input.equals("exit")) {
            log.warn("только 1, 2 или exit - повтори");
            input = console.nextLine();
        }
        switch (input) {
            case "1":
                for (Pet pet : PersonHolder.personHolder.get(wantPerson).getPets()) {
                    log.info("{}{}", pet.getName(), pet.getType());
                }
                ExitsUtils.informingBack();
                break;
            case "2":
                for (Pet pet : PersonHolder.personHolder.get(wantPerson).getPets()) {
                    log.info("{}{} -> ", pet.getName(), pet.getType());
                    pet.makeSound();
                }
                ExitsUtils.informingBack();
                break;
            case "exit":
                menuStack.pop();
                break;
            default:
                break;
        }
    }

    private void whatPersonWant() {
        log.info(PersonHolder.personHolder.keySet().toString());
        wantPerson = console.nextLine();
        while (!PersonHolder.personHolder.containsKey(wantPerson)) {
            log.warn("такого человека не найдено, не получится, введи еще раз как в списке");
            wantPerson = console.nextLine();
        }
    }
}
