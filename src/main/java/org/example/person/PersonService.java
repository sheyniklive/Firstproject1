package org.example.person;

import java.util.Scanner;

public class PersonService {
    String yourName;
    String yourSecName;

    public void askImFam() {
        Scanner console = new Scanner(System.in);
        System.out.println("братищщка, чекни имя: ");
        yourName = console.nextLine();
        System.out.println("Теперича - как род ваш именуют сударь: ");
        yourSecName = console.nextLine();
    }

    Person alice = new Person("Алиса", "Пупелкова", 35);

    public Person addPersons() {
        Person petr = new Person();
        petr.setName("Петр");
        petr.setSurname("Васильков");
        petr.setAge(35);
        return petr;
    }
}

