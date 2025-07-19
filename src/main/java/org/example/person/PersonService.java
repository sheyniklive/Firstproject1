package org.example.person;

import java.util.Scanner;

public class PersonService {
public PersonService () {}
    private static String askImFam() {
        Scanner console = new Scanner(System.in);
        System.out.println("братищщка, чекни имя: ");
        String name = console.nextLine();
        System.out.println("Теперича - как род ваш именуют сударь: ");
        String secname = console.nextLine();
        return "Твои имя-фамилия, бро: " + name + " " + secname;
    }
}
