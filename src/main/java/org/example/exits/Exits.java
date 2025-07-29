package org.example.exits;

import java.util.HashMap;
import java.util.Scanner;

public class Exits {
    public static HashMap<String, String> mapExits = new HashMap<>();
    Scanner console = new Scanner(System.in);

    static {
        mapExits.put("вежливо", "Хорошего дня, сеньор");
        mapExits.put("нормально", "пока");
        mapExits.put("грубо", "тинаотсюда//////");
    }

    public void addExits() {

        System.out.println("Привет,");
        System.out.println("введи 1 - чтобы скастомить свою реплику,");
        System.out.println("0 - пойдем в программу,");
        System.out.println("exit - вообще выйдем");
        String input = console.nextLine();
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

    public void doExit() {
        System.out.println("как с тобой попрощаться:");
        System.out.println("1 - вежливо,");
        System.out.println("2 - нормально,");
        System.out.println("3 - грубо,");
        System.out.println("4 - твой кастом");
        String choiseExit = console.nextLine();
        while (!choiseExit.equals("1") && !choiseExit.equals("2") && !choiseExit.equals("3") && !choiseExit.equals("4")) {
            System.out.println("только 1, 2, 3 или 4");
            choiseExit = console.nextLine();
        }
        switch (choiseExit) {
            case "1":
                System.out.println(mapExits.get("вежливо"));
                break;
            case "2":
                System.out.println(mapExits.get("нормально"));
                break;
            case "3":
                System.out.println(mapExits.get("грубо"));
                break;
            case "4":
                System.out.println("введи свой ключ:");
                String userKey = console.nextLine();
                while (!mapExits.containsKey(userKey)) {
                    System.out.println("похоже, ты где-то ошибся - введи еще раз по буквам))");
                    userKey = console.nextLine();
                }
                System.out.println(mapExits.get(userKey));
                break;
        }
        System.exit(0);
    }
}
