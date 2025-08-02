package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.calculator.CalculatorService;
import org.example.util.ExitsUtils;
import org.example.person.PersonService;

import java.util.Scanner;
@Slf4j
public class Main {
    static Scanner console = new Scanner(System.in);
    static String input;

    public static void main(String[] args) {
        ExitsUtils.addExits();
        System.out.println("хорошо, теперь выбери, чем ты хочешь заняться:");
        System.out.println("1 - Сам(а) введешь свои Имя и Фамилию или выведем сколько-то персонов,");
        System.out.println("2 - Калькулятор,");
        System.out.println("или введи exit для выхода");
        input = console.nextLine();
        while (!input.equals("exit") && !input.equals("1") && !input.equals("2")) {
            System.out.println("только 1, 2 или exit");
            input = console.nextLine();
        }
        switch (input) {
            case "1":
                PersonService.processPerson();
                break;
            case "2":
                CalculatorService.calculate();
                break;
            case "exit":
                ExitsUtils.doExit();
                break;
        }
    }
}