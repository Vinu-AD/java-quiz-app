package com.quizapp.ui;

import com.quizapp.service.QuizService;
import com.quizapp.service.UserService;
import com.quizapp.util.InputUtil;
import com.quizapp.service.QuestionService;
import com.quizapp.service.ScoreService;

public class AdminDashboard {

    private static final String ADMIN_PASSWORD = "admin@Vinu@3";

    private final QuestionService questionService = new QuestionService();
    private final ScoreService scoreService = new ScoreService();
    private final QuizService quizService = new QuizService();
    private final UserService userService = new UserService();

    public void adminValidation() {
        while (true) {
            System.out.print("Enter admin password (or 'Exit'): ");
            String password = InputUtil.getString();

            if(password.equalsIgnoreCase("exit")) return;

            if (password.equals(ADMIN_PASSWORD)) {
                System.out.println("Admin Login Successful!");
                adminMenu();
                break;
            }
            else {
                System.out.println("Incorrect password! Try again.");
            }
        }
    }

    public void adminMenu() {
        boolean loggedIn = true;

            while (loggedIn) {
                showMenu();

                try {
                    int choice = InputUtil.getInt(0, 6);

                    switch (choice) {
                        case 1:
                            questionService.createNewQuizFile(); // create new quiz file
                            break;
                        case 2:
                            questionService.alterQuestions(); // Alter existing question, options and answer
                            break;
                        case 3:
                            questionService.readQuestions(); // read the quizzes by topic wise
                            break;
                        case 4:
                            quizService.startQuiz("admin"); // demo quiz for checking corrections
                            break;
                        case 5:
                            scoreService.viewScore();  // view score separately
                            break;
                        case 6:
                            userService.usersCount();  // view score separately
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
        System.out.println("        Admin Dashboard");
        System.out.println("==============================");
        System.out.println("1. New quiz file");
        System.out.println("2. Alter existing file");
        System.out.println("3. View quizzes");
        System.out.println("4. Demo quiz");
        System.out.println("5. View scores");
        System.out.println("6. Total users count");
        System.out.println("0. Logout");
    }

}
