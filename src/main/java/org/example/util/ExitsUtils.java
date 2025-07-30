package org.example.util;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@UtilityClass
public class ExitsUtils {
    private static Map<String, String> mapExits = new HashMap<>();
    private static Scanner console = new Scanner(System.in);
    private static String chooseExit;
    private static String input;

    static {
        mapExits.put("вежливо", "Хорошего дня, сеньор");
        mapExits.put("нормально", "пока");
        mapExits.put("грубо", "тинаотсюда//////");
    }

    public static void addExits() {

        System.out.println("Привет, хочешь перед началом добавить свои варианты выходных реплик?");
        System.out.println("введи 1 - чтобы скастомить свою реплику,");
        System.out.println("0 - пойдем в программу,");
        System.out.println("exit - вообще выйдем");
        input = console.nextLine();
        while (!input.equals("1") && !input.equals("0") && !input.equals("exit")) {
            System.out.println("только 1, 0 или exit");
            input = console.nextLine();
        }
        switch (input) {
            case "1":
                do {
                    System.out.println("давай ключ: ");
                    String key = console.nextLine();
                    System.out.println("теперь фразу,которую ты получишь при выходе: ");
                    String value = console.nextLine();
                    mapExits.put(key, value);
                    System.out.println("хочешь добавить нового:");
                    System.out.println("пиши 'еще',");
                    System.out.println("любой другой ввод - пойдем в программу");
                    input = console.nextLine();
                } while (input.equals("еще"));
                break;
            case "0":
                break;
            case "exit":
                doExit();
                break;
        }
    }

    public static void doExit() {
        System.out.println("как с тобой попрощаться - пиши вариант из списка:");
        System.out.println(mapExits.keySet().toString());
        chooseExit = console.nextLine();
        while (!mapExits.containsKey(chooseExit)) {
            System.out.println("только ключ из списка");
            chooseExit = console.nextLine();
        }
        System.out.println(mapExits.get(chooseExit));
        System.exit(0);
    }
}
