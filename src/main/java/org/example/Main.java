package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.println("братищщка, чиркни имя: ");
        String name = console.nextLine();
        System.out.println("Теперича - как род ваш именуют сударь: ");
        String secname = console.nextLine();
        System.out.println("Твои имя-фамилия, бро: " + name + " " + secname);
    }
}