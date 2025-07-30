package org.example;

import org.example.calculator.CalculatorService;
import org.example.util.ExitsUtils;
import org.example.person.PersonService;

import java.util.Scanner;

public class Main {
    static Scanner console = new Scanner(System.in);
    static String input;
    static PersonService personService = new PersonService();
    static CalculatorService calculatorService = new CalculatorService();

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
                personService.processPerson();
                break;
            case "2":
                calculatorService.calculate();
                break;
            case "exit":
                ExitsUtils.doExit();
                break;
        }
    }
}