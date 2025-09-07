package com.quizapp.ui;

import com.quizapp.util.InputUtil;
import com.quizapp.service.QuizService;
import com.quizapp.service.ScoreService;
import com.quizapp.service.UserService;

public class UserDashboard {

    private final UserService userService = new UserService();
    private final QuizService quizService = new QuizService();
    private final ScoreService scoreService = new ScoreService();

    public void userChoice() {

        System.out.println();
        System.out.println("1. Login");
        System.out.println("2. Create New Account");
        System.out.println("0. Back to Main Menu");
        int choice = InputUtil.getInt(0, 2);

        try {
            switch (choice) {
                case 1:
                    String userName = userService.userValidation();
                    if(!userName.isEmpty())
                        userMenu(userName);
                    break;
                case 2:
                    String newUser = userService.createNewUser();
                    userMenu(newUser);
                    break;
                case 0:
                    System.out.println("Returning to Main Menu...");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void userMenu(String userName) {
        boolean loggedIn = true;

        while (loggedIn) {
            showMenu();

            try {
                int choice = InputUtil.getInt(0, 4);

                switch (choice) {
                    case 1:
                        quizService.startQuiz(userName);
                        break;
                    case 2:
                        scoreService.viewScoreByName(userName);
                        break;
                    case 3:
                        scoreService.viewTopScores();
                        break;
                    case 4:
                        userName = userService.profileUpdate(userName);
                        break;
                    case 0:
                        System.out.println("Logging out...");
                        loggedIn = false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void showMenu() {
        System.out.println();
        System.out.println("==============================");
        System.out.println("        User Dashboard");
        System.out.println("==============================");
        System.out.println("1. Start Quiz");
        System.out.println("2. My Scores");
        System.out.println("3. Top Scores");
        System.out.println("4. Change UserName or Password");
        System.out.println("0. Logout");
    }

}
