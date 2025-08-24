package com.quizapp.service;

import com.quizapp.util.FileUtil;
import com.quizapp.util.InputUtil;
import com.quizapp.model.Question;

import java.util.ArrayList;

public class QuestionService {

    public void addQuestions(String fileName) {
        ArrayList<Question> questions = getQuestions(); // getting n questions from the user
        FileUtil.addQuestion(questions, fileName);      // storing it in the file
    }

    public ArrayList<Question> getQuestions() {
        ArrayList<Question> questions = new ArrayList<>();

        while (true) {
            System.out.println();
            System.out.println("Enter question without options (or enter 'STOP' to skip or enter 'Multiline' for multiple line entries)");
            System.out.print("Enter: ");
            String question = InputUtil.getString();

            if(question.trim().equalsIgnoreCase("stop")) break;

            if(question.trim().equalsIgnoreCase("multiline")) {
                questions.add(getMultiLineQuestion());
                continue;
            }

            ArrayList<String> options = new ArrayList<>();
            System.out.println("Enter content for option(a)");
            options.add(InputUtil.getString());
            System.out.println("Enter content for option(b)");
            options.add(InputUtil.getString());

            int order = 3;

            while (true) {
                System.out.println("\nEnter content for option(" + (char)(order+96) + "). (Enter 'stop' to finish options)");
                String value = InputUtil.getString();
                if(value.equalsIgnoreCase("stop")) {
                    break;
                }
                options.add(value);
                order++;
            }

            System.out.println("\nEnter the correct answer(a-"+ (char)(options.size()+96) +")");
            char answer = InputUtil.getChar('a', (char) (options.size() + 96));

            questions.add(new Question(question, options, answer));

            System.out.println("\nDo you want to add more question?");
            System.out.println("(1. Yes, 2. No)");

            int option = InputUtil.getInt(1, 2);
            if(option == 2) break;

        }
        return questions;
    }

    public Question getMultiLineQuestion() {

        System.out.println();
        System.out.println("Enter/paste the question (enter 'END' on a new line to finish question)");

        String question = InputUtil.getPara() + '\n';
        ArrayList<String> options = new ArrayList<>();

        char order = 'a';

        while (true) {
            System.out.println("\nEnter/paste the content for option(" + order + "). " +
                    "(enter 'END' on a new line to finish option "+ order +")");
            if(order-96 > 2)
                System.out.println("(Enter 'STOP' to finish options)");
            String value = InputUtil.getPara();
            if (value.trim().equalsIgnoreCase("stop")) break;
            options.add('\n' + value + '\n');
            order++;
        }

        System.out.println("Enter the correct answer (option(a-"+ order +") ");

        char answer = InputUtil.getChar('a', order);
        return new Question(question, options, answer);
    }

    public void alterQuestions() {
        int nthFile = chooseFile(); // choosing the file to alter
        if(nthFile == 0)
            throw new RuntimeException("No files found to alter");

        while (true) {
            alterMenu(); // it shows menu

            int choice = InputUtil.getInt(0, 3);

            if (choice == 0) return;

            if (choice == 1) {
                ArrayList<Question> questions = getQuestions();
                FileUtil.appendQuestion(nthFile, questions);
                System.out.println("Question appended successfully");
            } else if (choice == 2) {
                FileUtil.deleteQuestion(nthFile);
            } else if (choice == 3) {
                FileUtil.updateQuestion(nthFile);
            }
            else break;
        }
    }

    public void alterMenu() {
        System.out.println();
        System.out.println("What do you want to perform?");
        System.out.println("1. Append");
        System.out.println("2. Delete");
        System.out.println("3. Update");
        System.out.println("0. <<< Back to Main Menu");
    }

    public void readQuestions() {
        try {
            int nthFile = chooseFile();
            if(nthFile == 0) {
                throw new RuntimeException();
            }
            FileUtil.readFile(nthFile);
        } catch (Exception e) {
            throw new RuntimeException("No questions found");
        }
    }

    public int chooseFile() {
        System.out.println("\nChoose your file choice");
        int fileCount = FileUtil.showQuizTypes();
        if(fileCount == 0) {
            return 0;
        }
        return InputUtil.getInt(1, fileCount);
    }
}
