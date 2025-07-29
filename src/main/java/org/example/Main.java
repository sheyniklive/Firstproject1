package org.example;

import org.example.calculator.CalculatorService;
import org.example.exits.Exits;
import org.example.person.PersonService;

import java.util.Scanner;

public class Main {
    static Scanner console = new Scanner(System.in);
    static String input;
    static PersonService personService = new PersonService();
    static CalculatorService calculatorService = new CalculatorService();
    static Exits exits = new Exits();


    public static void main(String[] args) {
        System.out.println("Привет, хочешь перед началом добавить свои варианты выходных реплик?");
        System.out.println("1 - да,");
        System.out.println("0 - идем дальше");
        System.out.println("exit - вообще выйдем");
        input = console.nextLine();
        while (!input.equals("1") && !input.equals("0") && !input.equals("exit")) {
            System.out.println("только 1, 0 или exit");
            input = console.nextLine();
        }
        switch (input) {
            case "1":
                exits.addExits();
                break;
            case "0":
                break;
            case "exit":
                exits.doExit();
                break;
        }
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
                exits.doExit();
                break;
        }
    }
}