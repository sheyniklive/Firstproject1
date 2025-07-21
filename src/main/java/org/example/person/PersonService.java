package org.example.person;

import java.util.Scanner;

public class PersonService {
    String name;
    String secname;

    public String askImFam() {
        Scanner console = new Scanner(System.in);
        System.out.println("братищщка, чекни имя: ");
        name = console.nextLine();
        System.out.println("Теперича - как род ваш именуют сударь: ");
        secname = console.nextLine();
        return "Твои имя-фамилия, бро: " + name + " " + secname;
    }
}
