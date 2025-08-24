package com.quizapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Score implements Serializable, Comparable<Score> {
    private String name;
    private String quizType;
    private int score;
    private int totalQuestions;
    private float percentage;
    private LocalDateTime dateTime;

    public Score(String name, String quizType, int score, int totalQuestions, float percentage, LocalDateTime dateTime) {
        this.name = name;
        this.quizType = quizType;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public String getQuizType() {
        return quizType;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public float getPercentage() {
        return percentage;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm:ss a");
        return String.format("%-10s | %-20s | %4d / %-4d | %-10.2f | %s", name, quizType, score, totalQuestions, percentage, dateTime.format(formatter));
    }

    @Override
    public int compareTo(Score ob) {
        int n = Float.compare(ob.percentage, this.percentage);
        if (n == 0)
            return this.dateTime.compareTo(ob.dateTime);
        else
            return n;
    }
}
