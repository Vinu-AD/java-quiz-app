package com.quizapp.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private String question;
    private ArrayList<String> options;
    private char answer;

    @Serial
    private static final long serialVersionUID = -1234567890L;

    public Question() {}

    public Question(String question, ArrayList<String> options, char answer) {
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public char getAnswer() {
        return answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public void setAnswer(char answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        StringBuilder qn = new StringBuilder();
        qn.append("Qn. ").append(question).append('\n');
        char order = 'a';
        for(String option : options) {
            qn.append(order).append(") ").append(option).append('\n');
            order++;
        }
        qn.append("Answer: ").append(answer).append('\n');
        return qn.toString();
    }
}
