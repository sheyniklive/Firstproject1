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
        System.out.println("2 - создадим персона или несколько");
        System.out.println("или выйти через exit");
        switch (console.nextLine()) {
            case "1":
                System.out.println("братищщка, чекни имя: ");
                yourName = console.nextLine();
                System.out.println("Теперича - как род ваш именуют сударь: ");
                yourSecName = console.nextLine();
                System.out.println("Тебя зовут - " + yourName + " " + yourSecName);
                break;
            case "2":
                System.out.println("1 - создать персона(ов) или exit - для котопультирования из программы");
                switch (console.nextLine()) {
                    case "1":
                        addPersons();
                        break;
                    case "exit":
                        System.out.println("уже скучаю, щегол");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("надо было 1, 2 или exit - перезапусти прорамму");
                        break;
                }
                break;
            case "exit":
                System.out.println("адьос)");
                System.exit(0);
                break;
            default:
                System.out.println("1, 2 or exit - не попал, перезапусти");
                break;
        }
    }


    private void addPersons() {
        System.out.println("введи имя:");
        String name = console.nextLine();
        System.out.println("фамилию:");
        String surname = console.nextLine();
        System.out.println("возраст");
        int age = console.nextInt();
        console.nextLine();
        System.out.println("сколько таких ты хочешь?");
        int n = console.nextInt();
        console.nextLine();
        if (n == 1) {
            var person = List.of( new Person (name, surname, age) );
            System.out.println(person);
        } else {
            if (n > 0) {
                var persons = new ArrayList<Person>();
                for (int i = 0; i < n; i++) {
                    Person person = new Person(name + "_" + i, surname + "_" + i, age);
                    persons.add(person);
                }
                System.out.println(persons);
            } else {
                System.out.println("не сильно-то и хочешь, пока");
                System.exit(0);
            }
        }
    }
}
