package org.example.pet;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.person.PersonHolder;

import java.util.Scanner;

@UtilityClass
@Slf4j
public class PetService {
    static Scanner console = new Scanner(System.in);
    static String input;
    static String wantPerson;

    public static void processPetService() {
        log.info("выбирай:" +
                "1 - пойдем к чьим-то питомцам," +
                "2 - можем добавить кому-нибудь из людей новых" +
                "или 'back' для возврата");
        input = console.nextLine();
        while (!input.equals("back") && !input.equals("1") && !input.equals("2")) {
            log.warn("только 1, 2 или 'back'- повтори");
            input = console.nextLine();
        }
        switch (input) {
            case "1":
                getPets();
                break;
            case "2":
                addPets();
                break;
            case "back":
                // back
                break;
        }
    }

    public static void addPets() {
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
            }
            log.info("хочешь добавить нового:");
            log.info("пиши 'еще',");
            log.info("любой другой ввод - закончим");
            input = console.nextLine();
        } while (input.equals("еще"));
    }

    public static void getPets() {
        log.info("с чьими животными ты хочешь взаимодействовать?");
        whatPersonWant();
        //продолжить
    }

    private static void whatPersonWant() {
        log.info(PersonHolder.personHolder.keySet().toString());
        wantPerson = console.nextLine();
        while (!PersonHolder.personHolder.containsKey(wantPerson)) {
            log.warn("такого человека не найдено, не получится, введи еще раз как в списке");
            wantPerson = console.nextLine();
        }
    }
}
