package com.quizapp.service;

import com.quizapp.util.FileUtil;
import com.quizapp.util.InputUtil;
import com.quizapp.model.Score;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class ScoreService {

    private void scoreMenu() {
        System.out.println();
        System.out.println("Choose your choice");
        System.out.println("1. View Score by Name");
        System.out.println("2. View Score by Quiz Topic");
        System.out.println("3. View all Scores by Date");
        System.out.println("4. View top Scores");
        System.out.println("5. View recent Scores");
        System.out.println("0. <<< Back Menu");
    }

    public void viewScore() {
        scoreMenu();

        int choice = InputUtil.getInt(0, 5);

        switch (choice) {
            case 0:
                return;
            case 1:
                ArrayList<String> users = new ArrayList<>(FileUtil.getUsers());
                System.out.println("\nChoose UserName to view score");

                for (int i = 1; i <= users.size(); i++)
                    System.out.println(i + ". " + users.get(i - 1));

                viewScoreByName(users.get(InputUtil.getInt(1, users.size()) - 1));
                break;
            case 2:
                ArrayList<String> quizTypes = FileUtil.getQuizTypes();
                if (quizTypes.isEmpty())
                    throw new NoSuchElementException("No quiz types found");

                System.out.println("\nChoose Quiz type to view score");
                for (int i = 1; i <= quizTypes.size(); i++)
                    System.out.println(i + ". " + quizTypes.get(i - 1).substring(0, quizTypes.get(i - 1).length() - 4));

                viewScoreByQuizType(quizTypes.get(InputUtil.getInt(1, quizTypes.size()) - 1));
                break;
            case 3:
                viewAllScores();
                break;
            case 4:
                viewTopScores();
                break;
            case 5:
                viewRecentScores();
                break;
        }
    }

    public void viewScoreByName(String userName) {
        ArrayList<Score> scores = FileUtil.getScore();

        if (scores == null) {
            System.out.println("No scores found");
            return;
        }
        Collections.sort(scores);
        int totalAttempt = 0;

        System.out.printf("%-15s | %-30s | %-10s | %-10s | %s\n", "Name", "Quiz Topic", "Score/Total", "Percentage", "Attended Date and Time");
        System.out.println("=============== | ============================== | =========== | ========== | ========================");
        for (Score score : scores) {
            if (score.getName().equals(userName)) {
                totalAttempt++;
                System.out.println(score);
            }
        }

        if(totalAttempt == 0) System.out.println("No scores found");
        System.out.println("\nTotal attempt count : " + totalAttempt);
    }

    public void viewScoreByQuizType(String quizType) {
        ArrayList<Score> scores = FileUtil.getScore();

        if (scores == null) {
            System.out.println("No scores found");
            return;
        }

        Collections.sort(scores);
        int totalAttempt = 0;

        System.out.printf("%-15s | %-30s | %-10s | %-10s | %s\n", "Name", "Quiz Topic", "Score/Total", "Percentage", "Attended Date and Time");
        System.out.println("=============== | ============================== | =========== | ========== | ========================");

        for (Score score : scores) {
            if (score.getQuizType().equals(quizType.substring(0, quizType.length() - 4))) {
                totalAttempt++;
                System.out.println(score);
            }
        }

        if(totalAttempt == 0) System.out.println("No scores found");
        System.out.println("\nTotal attempt count : " + totalAttempt);
    }

    public void viewAllScores() {
        ArrayList<Score> scores = FileUtil.getScore();

        if (scores == null) {
            System.out.println("No scores found");
            return;
        }
        int totalAttempt = 0;
        System.out.printf("%-15s | %-30s | %-10s | %-10s | %s\n", "Name", "Quiz Topic", "Score/Total", "Percentage", "Attended Date and Time");
        System.out.println("=============== | ============================== | =========== | ========== | ========================");
        for (Score score : scores) {
            totalAttempt++;
            System.out.println(score);
        }
        if(totalAttempt == 0) System.out.println("No scores found");
        System.out.println("\nTotal attempt count : " + totalAttempt);
    }

    public void viewTopScores() {

        ArrayList<Score> scores = FileUtil.getScore();

        if (scores == null) {
            System.out.println("No scores found");
            return;
        }

        System.out.println("Enter the Top scores count (1-"+ scores.size() +") ");
        int top = InputUtil.getInt(1, scores.size());

        Collections.sort(scores);

        System.out.println("\nTop "+ top +" Scores are:\n");

        System.out.printf("%-15s | %-30s | %-10s | %-10s | %s\n", "Name", "Quiz Topic", "Score/Total", "Percentage", "Attended Date and Time");
        System.out.println("=============== | ============================== | =========== | ========== | ========================");
        for (int i = 0; i < top; i++) {
            System.out.println(scores.get(i));
        }
    }

    public void viewRecentScores() {
        ArrayList<Score> scores = FileUtil.getScore();

        if (scores == null) {
            System.out.println("No scores found");
            return;
        }

        Comparator<Score> compareByDate = (o1,o2) -> o2.getDateTime().compareTo(o1.getDateTime());

        scores.sort(compareByDate);

        System.out.println("Enter the recent scores count (1-"+ scores.size() +") ");
        int recent = InputUtil.getInt(1, scores.size());

        System.out.println("\nRecent "+ recent +" Scores are:\n");

        System.out.printf("%-15s | %-30s | %-10s | %-10s | %s\n", "Name", "Quiz Topic", "Score/Total", "Percentage", "Attended Date and Time");
        System.out.println("=============== | ============================== | =========== | ========== | ========================");
        for (int i = 0; i < recent; i++) {
            System.out.println(scores.get(i));
        }
    }

    public void saveScore(String userName, String quizType, int mark, int attended, float percentage) {
        Score newScore = new Score(userName, quizType, mark, attended, percentage, LocalDateTime.now());
        FileUtil.writeScore(newScore);
    }

}
