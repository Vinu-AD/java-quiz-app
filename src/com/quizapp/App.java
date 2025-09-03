package com.quizapp;

import com.quizapp.ui.MainMenu;

public class App {
    public static void main(String[] args) {
        System.out.println("===================================");
        System.out.println("    Welcome to Quiz Application   ");
        System.out.println("===================================");

        MainMenu mainMenu = new MainMenu();
        mainMenu.start();
    }
}




