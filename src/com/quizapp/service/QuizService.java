package com.quizapp.service;

import com.quizapp.util.FileUtil;
import com.quizapp.util.InputUtil;
import com.quizapp.model.Question;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class QuizService {

    public void startQuiz(String userName) {
        System.out.println();
        System.out.println("Choose your choice");
        System.out.println("1. Timed Quiz");
        System.out.println("2. Self-Paced Quiz");
        System.out.println("0. <<< Back to Main Menu");

        int choice = InputUtil.getInt(0, 2);

        switch (choice) {
            case 0:
                return;
            case 1:
                timedQuiz(userName);
                break;
            case 2:
                selfPacedQuiz(userName);
                break;
        }
    }

    public void timedQuiz(String userName) {
        System.out.println();
        System.out.println("Choose your topic");
        int topicCount = FileUtil.showQuizTypes();
        if (topicCount == 0) {
            throw new RuntimeException("No quizzes added");
        }

        System.out.println("0. <<< Back Menu");
        int chosenTopic = InputUtil.getInt(0, topicCount);
        if(chosenTopic == 0) return;

        ArrayList<Question> questions = FileUtil.getQuestions(chosenTopic);
        if (questions == null) {
            System.out.println("No questions found");
            return;
        }

        Collections.shuffle(questions); // shuffling the order to feel as new question in everytime

        System.out.println("Enter the number of questions do you want to attend (out of " + questions.size() + ") ");
        int count = InputUtil.getInt(1, questions.size());

        Object[] result = conductTimedQuiz(questions, count, timePreference());
        int correctAnswer = (int) result[0];
        LinkedHashMap<Question, Character > summary = (LinkedHashMap<Question, Character>) result[1];
        int total = questions.size();
        int attempted = summary.size();
        float percentage = ((float)correctAnswer/total) * 100;
        String quizType = FileUtil.getQuizName(chosenTopic);

        displayReport(userName, quizType, correctAnswer, attempted, total, percentage);

        if(!userName.equals("admin"))
            new ScoreService().saveScore(userName, quizType, correctAnswer, total, percentage);

        viewSummary(correctAnswer, summary);
        System.out.println("Exiting from the quiz...");
    }

    public Object[] conductTimedQuiz(ArrayList<Question> questions, int count, int second) {
        int correctAnswer = 0;
        LinkedHashMap<Question, Character> correct = new LinkedHashMap<>();
        LinkedHashMap<Question, Character> wrong = new LinkedHashMap<>();

        for (int qn = 1; qn <= count; qn++) {
            Question question = questions.get(qn-1);
            System.out.println("=====================================================================================");
            System.out.println("Qn " + qn + " out of " + count);
            System.out.println(question.getQuestion());

            char order = 'a';
            ArrayList<String> options = question.getOptions();
            for (String option : options) {
                System.out.println(order++ + ") " + option);
            }

            System.out.println("\nEnter the correct option(a-"+ (--order) +") below");
            char selectedOption = ' ';
            LocalTime end = LocalTime.now().plusSeconds(second);
            int timeLeft = second;

            while (LocalTime.now().isBefore(end)) {
                if (InputUtil.ifPresent()) {
                    selectedOption = InputUtil.getChar('a', order); // order will be at the end of the options list
                    break;
                }

                System.out.print("\rTime left: " + timeLeft-- + "s Enter: "); // \r overwrites same line
                System.out.flush();

                try { Thread.sleep(1000); }
                catch (InterruptedException e) { throw new RuntimeException(e); }
            }
            System.out.println();

            if (selectedOption == question.getAnswer()) {
                correctAnswer++;
                correct.put(question, question.getAnswer());
            }
            else if(selectedOption == ' ') {
                System.out.println("Time up!");
                wrong.put(question, ' ');
            }
            else
                wrong.put(question, selectedOption);
        }
        correct.putAll(wrong);
        return new Object[]{correctAnswer, correct};
    }

    private static int timePreference() {
        System.out.println("\nChoose the difficulty level");
        System.out.println("1. Easy (30s)");
        System.out.println("2. Medium (20s)");
        System.out.println("3. Hard (10s)");
        System.out.println("0. Custom");

        int preference = InputUtil.getInt(0, 3);
        int time;
        if (preference == 0) {
            System.out.println("Enter custom seconds (1-60): ");
            time = InputUtil.getInt(1, 60);
        }
        else
            time = preference == 1 ? 30 : preference == 2 ? 20: 10;

        System.out.println("Note: You have to enter the correct answer option and 'Enter key' within a second");
        System.out.println("Press 'Enter key' to continue");
        InputUtil.getEnter();

        return time;
    }

    public void selfPacedQuiz(String userName) {
        System.out.println();
        System.out.println("Choose your topic");
        int topicCount = FileUtil.showQuizTypes();
        if (topicCount == 0) {
            throw new RuntimeException("No quizzes added");
        }

        System.out.println("0. <<< Back Menu");
        int chosenTopic = InputUtil.getInt(0, topicCount);
        if(chosenTopic == 0) return;

        ArrayList<Question> questions = FileUtil.getQuestions(chosenTopic);
        if (questions == null) {
            System.out.println("No questions found");
            return;
        }

        Collections.shuffle(questions); // shuffling the order to feel as new question in everytime

        Object[] result = conductQuiz(questions);
        int correctAnswer = (int) result[0];
        LinkedHashMap<Question, Character > summary = (LinkedHashMap<Question, Character>) result[1];
        int total = questions.size();
        int attempted = summary.size();
        float percentage = ((float)correctAnswer/total) * 100;
        String quizType = FileUtil.getQuizName(chosenTopic);

        displayReport(userName, quizType, correctAnswer, attempted, total, percentage);

        if(!userName.equals("admin"))
            new ScoreService().saveScore(userName, quizType, correctAnswer, total, percentage);

        viewSummary(correctAnswer, summary);
        System.out.println("Exiting from the quiz...");
    }

    public Object[] conductQuiz(ArrayList<Question> questions) {
        int correctAnswer = 0;
        LinkedHashMap<Question, Character> correct = new LinkedHashMap<>();
        LinkedHashMap<Question, Character> wrong = new LinkedHashMap<>();
        int questionNumber = 0, total = questions.size();
        for (Question question : questions) {
            System.out.println("=====================================================================================");
            System.out.println("Qn " + ++questionNumber + " out of " + total);
            System.out.println(question.getQuestion());

            char order = 'a';
            ArrayList<String> options = question.getOptions();
            for (String option : options) {
                System.out.println(order++ + ") " + option);
            }

            System.out.println("\nEnter the correct option(a-"+ (--order) +") below");
            char selectedOption = InputUtil.getChar('a', order); // order will be at the end of the options list

            if (selectedOption == question.getAnswer()) {
                correctAnswer++;
                correct.put(question, question.getAnswer());
            }
            else
                wrong.put(question, selectedOption);

            System.out.println();
            System.out.println("1. Show answer");
            System.out.println("2. Next question");
            System.out.println("3. End quiz");
            int choice = InputUtil.getInt(1, 3);

            if(choice == 1) {
                System.out.println("\nCorrect Answer: " + question.getAnswer() + ") " + question.getOptions().get(question.getAnswer()-97));
                if(selectedOption == question.getAnswer())
                    System.out.println("You selected \"CORRECT\" answer ("+ question.getAnswer() +")");
                else
                    System.out.println("You selected \"WRONG\" answer ("+ selectedOption +")");

                System.out.println("Press 'Enter Key' to continue");
                InputUtil.getEnter();
            }
            else if (choice == 3) break;
        }
        correct.putAll(wrong);
        return new Object[]{correctAnswer, correct};
    }

    private void displayReport(String userName, String quizType, int correct, int attempted, int total, float percentage) {
        System.out.println();
        System.out.println("===================================");
        System.out.println("            QUIZ REPORT");
        System.out.println("===================================");
        System.out.printf("%-12s : %s\n", "Attendee", userName);
        System.out.printf("%-12s : %s\n", "Quiz Topic", quizType);
        System.out.printf("%-12s : %d\n", "Total Qns", total);
        System.out.printf("%-12s : %d\n", "Attempted", attempted);
        System.out.printf("%-12s : %d\n", "Correct", correct);
        System.out.printf("%-12s : %d\n", "Wrong", attempted - correct);
        System.out.printf("%-12s : %d / %d\n", "Score", correct, total);
        System.out.printf("%-12s : %.2f%%\n", "Percentage", percentage);
        System.out.println("===================================");
    }

    private void viewSummary(int correctAnswer, LinkedHashMap<Question, Character> summary) {
        System.out.println("Do you want to review your answers?");

        if (InputUtil.getYesNo()) {
            ArrayList<Question> questions = new ArrayList<>(summary.keySet());

            System.out.println("==========================================================================================================");
            System.out.println("                                       Correct Answers");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            if(correctAnswer == 0) {
                System.out.println("No correct answers found");
                System.out.println("----------------------------------------------------------------------------------------------------------");
            }
            for (int i = 0; i < correctAnswer; i++) {
                Question question = questions.get(i);
                System.out.println((i+1) + ". " + question.getQuestion());
                System.out.println("Correct answer : " + question.getAnswer() + ") " + question.getOptions().get(question.getAnswer() - 97));
                System.out.println("You selected   : " + summary.get(question) + ") " + question.getOptions().get(summary.get(question)- 97));
                System.out.println("----------------------------------------------------------------------------------------------------------");
            }

            System.out.println("==========================================================================================================");
            System.out.println("                                     Wrong Answers");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            if(questions.size() - correctAnswer == 0) {
                System.out.println("No wrong answers found");
                System.out.println("----------------------------------------------------------------------------------------------------------");
            }

            for (int i = correctAnswer; i < questions.size(); i++) {
                Question question = questions.get(i);
                System.out.println((i-correctAnswer+1) + ". " + question.getQuestion());
                System.out.println("Correct answer : " + question.getAnswer() + ") " + question.getOptions().get(question.getAnswer() - 97));
                if (summary.get(question) == ' ')
                    System.out.println("You selected : N/A (time up)");
                else
                    System.out.println("You selected   : " + summary.get(question) + ") " + question.getOptions().get(summary.get(question)- 97));
                System.out.println("----------------------------------------------------------------------------------------------------------");
            }
        }
    }

}
