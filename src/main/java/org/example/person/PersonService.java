package org.example.person;

import java.util.Scanner;

public class PersonService {

    String yourName;
    String yourSecName;
    Scanner console = new Scanner(System.in);


    public void processPerson() {
        System.out.println("1 - ты хочешь вручную ввести свои Ф-И,");
        System.out.println("2 - создадим персона");
        switch (console.nextLine()) {
            case "1":
                System.out.println("братищщка, чекни имя: ");
                yourName = console.nextLine();
                System.out.println("Теперича - как род ваш именуют сударь: ");
                yourSecName = console.nextLine();
                System.out.println("Тебя зовут - " + yourName + " " + yourSecName);
                break;
            case "2":
                System.out.println("выбирай: через конструктор - 1 или сеттеры - 2?");
                switch (console.nextLine()) {
                    case "1":
                        System.out.println("введи имя:");
                        String name = console.nextLine();
                        System.out.println("фамилию:");
                        String surname = console.nextLine();
                        System.out.println("возраст");
                        int age = console.nextInt();
                        Person person1 = new Person(name, surname, age);
                        System.out.println(person1.toString());
                        break;
                    case "2":
                        Person person2 = new Person();
                        System.out.println("вводи имя:");
                        person2.setName(console.nextLine());
                        System.out.println("фамилию:");
                        person2.setSurname(console.nextLine());
                        System.out.println("возраст:");
                        person2.setAge(console.nextInt());
                        System.out.println(person2.toString());
                        break;
                }
                break;
        }
    }
}