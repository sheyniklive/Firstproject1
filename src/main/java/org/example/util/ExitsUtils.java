package org.example.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

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
        log.info("любой другой ввод - пойдем в программу,");
        log.info("exit - вообще выйдем");
        input = console.nextLine();
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
                log.info("любой другой ввод - пойдем в программу");
                input = console.nextLine();
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
        if (console.nextLine().equalsIgnoreCase("exit")) {
            menuStack.removeLast();
        }
    }
}
