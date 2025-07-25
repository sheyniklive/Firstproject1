package org.example.person;

import java.util.ArrayList;
import java.util.List;
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
                System.out.println("выбирай: одного - 1 или пачку - 2?");
                switch (console.nextLine()) {
                    case "1":
                        addOnePerson();
                        break;
                    case "2":

                        break;
                }
                break;
        }
    }

    private void addOnePerson() {
        Person person1 = new Person();
        System.out.println("вводи имя:");
        person1.setName(console.nextLine());
        System.out.println("фамилию:");
        person1.setSurname(console.nextLine());
        System.out.println("возраст:");
        person1.setAge(console.nextInt());
        System.out.println(person1.toString());
    }

    private void addListPerson() {
        System.out.println("введи имя:");
        String name = console.nextLine();
        System.out.println("фамилию:");
        String surname = console.nextLine();
        System.out.println("возраст");
        int age = console.nextInt();
        System.out.println("сколько таких ты хочешь?");
        int n = console.nextInt();
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            persons.add(new Person(name, surname, age));
        }
        System.out.println(persons.toString());
    }
}