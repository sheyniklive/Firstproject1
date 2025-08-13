package org.example.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidMenuChoiceException;
import org.example.validator.Validators;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.example.Main.menuStack;

@UtilityClass
@Slf4j
public class ExitsUtils {
    private static final Map<String, String> mapExits = new HashMap<>();
    private static final Scanner console = new Scanner(System.in);


    static {
        mapExits.put("вежливо", "Хорошего дня, сеньор");
        mapExits.put("нормально", "пока");
        mapExits.put("грубо", "тинаотсюда//////");
    }

    public static void addExits() {
        String input;
        log.info("Привет, хочешь перед началом добавить свои варианты выходных реплик?");
        log.info("введи 1 - чтобы скастомить свою реплику,");
        log.info("2 - пойдем в программу,");
        log.info("exit - вообще выйдем");
        while (true) {
            try {
                input = console.nextLine();
                Validators.choiceServicesMenu.validate(input);
                break;
            } catch (InvalidMenuChoiceException e) {
                log.error("Ошибка выбора меню в ExitUtils`е", e);
                log.info("попробуй еще раз: 1, 2 или exit");
            }
        }
        if (input.equals("exit")) {
            doExit();
            return;
        }
        if (input.equals("1")) {
            do {
                log.info("давай ключ: ");
                String key = console.nextLine();
                log.info("теперь фразу,которую ты получишь при выходе: ");
                String value = console.nextLine();
                mapExits.put(key, value);
                log.info("хочешь добавить новую:");
                log.info("1 - да,");
                log.info("0 - пойдем в программу");
                while (true) {
                    try {
                        input = console.nextLine();
                        Validators.yesNo.validate(input);
                        break;
                    } catch (InvalidMenuChoiceException e) {
                        log.error("Ошибка выбора действия", e);
                        log.info("повтори: 1 или 0");
                    }
                }
            } while (input.equals("1"));
        }
    }

    public static void doExit() {
        String chooseExit;
        log.info("как с тобой попрощаться - пиши вариант из списка:");
        log.info(mapExits.keySet().toString());
        chooseExit = console.nextLine();
        while (!mapExits.containsKey(chooseExit)) {
            log.warn("только ключ из списка - повтори ввод");
            chooseExit = console.nextLine();
        }
        log.info(mapExits.get(chooseExit));
        System.exit(0);
    }

    public static void informingBack() {
        log.info("теперь можешь вернуться в прошлое меню через exit");
        String input = console.nextLine();
        while (!input.equals("exit")) {
            log.warn("пытаешься ввести что-то другое, попробуй еще");
            input = console.nextLine();
        }
        menuStack.removeLast();
    }
}
