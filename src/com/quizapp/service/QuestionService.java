package com.quizapp.service;

import com.quizapp.util.FileUtil;
import com.quizapp.util.InputUtil;
import com.quizapp.model.Question;

import java.util.ArrayList;

public class QuestionService {

    public void alterMenu() {
        System.out.println();
        System.out.println("What do you want to perform?");
        System.out.println("1. Append");
        System.out.println("2. Delete");
        System.out.println("3. Update");
        System.out.println("0. <<< Back to Main Menu");
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
                appendQuestion(nthFile);
            } else if (choice == 2) {
                deleteQuestion(nthFile);
            } else if (choice == 3) {
                updateQuestion(nthFile);
            }
            else break;
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

    public void readQuestions() {
        try {
            int nthFile = chooseFile();
            if(nthFile == 0) {
                throw new RuntimeException();
            }
            FileUtil.readQuestionsFromFile(nthFile);
        } catch (Exception e) {
            throw new RuntimeException("No questions found");
        }
    }

    public void appendQuestion(int nthFile) {
        ArrayList<Question> questions = getQuestionsFromUser();
        if (questions == null) return;

        // reading the existing questions, for appending at the end
        ArrayList<Question> existingQuestions = FileUtil.getQuestionsFromFile(nthFile);

        existingQuestions.addAll(questions); // adding new questions to the existing

        FileUtil.putQuestionsToFile(nthFile, existingQuestions); // pushing it to the same file

        System.out.println("Question appended successfully");
    }

    public void createNewQuizFile() {
        while (true) {
            System.out.print("Enter the new quiz name: ");
            String fileName = InputUtil.getString();

            if(fileName.equalsIgnoreCase("exit"))
                return;

            boolean isFileCreated = FileUtil.createQuizFile(fileName);

            if (isFileCreated) {
                // add some questions in the same file
                FileUtil.addQuestions(fileName, getQuestionsFromUser());
                System.out.println("New quiz file created successfully.");
                break;
            }
            else {
                System.out.println("File already exists.");
                System.out.println("Try another name or type 'exit' to cancel");
            }
        }
    }

    public ArrayList<Question> getQuestionsFromUser() {
        ArrayList<Question> questions = new ArrayList<>();

        while (true) {
            System.out.println();
            System.out.println("Enter question without options (or enter 'STOP' to skip or enter 'Multiline' for multiple line entries)");
            System.out.print("Enter: ");
            String question = InputUtil.getString();

            if(question.trim().equalsIgnoreCase("stop")) return null;

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

    public static void deleteQuestion(int nthFile) {
        ArrayList<Question> questions = FileUtil.getQuestionsFromFile(nthFile);
        int questionsCount = questions.size();

        // deleting the questions
        while (true) {
            readQuestions(questions);

            System.out.println("\nWhich question do you want to delete? (0-skip)");
            int questionNumber = InputUtil.getInt(0, questionsCount);

            if (questionNumber == 0) break;

            questions.remove(questionNumber - 1);
            System.out.println("Question number " + questionNumber + " removed\n");
            questionsCount--;

            System.out.println("Do you want to delete more question?");
            if (!InputUtil.getYesNo()) break;
        }

        // pushing the remaining question to the same file
        FileUtil.putQuestionsToFile(nthFile, questions);
    }

    public static void readQuestions(ArrayList<Question> questions) {
        if(questions == null) throw new RuntimeException("Empty file");

        int order = 1;
        System.out.println("===================================================================================");
        for(Question question : questions) {
            System.out.println(order++ + ". " + question.getQuestion());

            char optionOrder = 'a';
            for (String option : question.getOptions()) {
                System.out.println(optionOrder++ + ") " + option);
            }

            System.out.println("Answer: " + question.getAnswer() + ") " + question.getOptions().get(question.getAnswer() - 97));
            System.out.println("===================================================================================");
        }
    }

    public static void updateQuestion(int nthFile) {

        // fetching the questions from the file
        ArrayList<Question> questions = FileUtil.getQuestionsFromFile(nthFile);
        if (questions == null) {
            System.out.println("Empty file");
            return;
        }

        int questionsCount = questions.size();

        // updating the questions
        while (true) {
            readQuestions(questions);

            System.out.println("Which question do you want to update? (0-skip)");
            int questionNumber = InputUtil.getInt(0, questionsCount);

            if(questionNumber == 0) break;

            while (true) {
                // retrieving the questions to update
                Question actualQuestion = questions.get(questionNumber - 1);
                System.out.print("\n" + actualQuestion);

                System.out.println("\nWhat do you want to update?");
                System.out.println("1. Question\n2. Options\n3. Answer");
                int choice = InputUtil.getInt(1, 3);

                // updating the question
                if (choice == 1) {
                    System.out.println("Insert the new(corrected) question");
                    System.out.println("(enter 'Multiline' for multiple line questions)");
                    String newQuestion = InputUtil.getString();
                    if (newQuestion.equalsIgnoreCase("multiline")) {
                        newQuestion = getMultilineQuestion();
                    }

                    actualQuestion.setQuestion(newQuestion); // updating the new question
                    questions.set(questionNumber - 1, actualQuestion); // updating updated question to the questions list

                    // for question confirmation
                    System.out.println("\nActual question after modification");
                    System.out.println(actualQuestion);
                }

                // updating the options
                else if (choice == 2) {

                    ArrayList<String> options = actualQuestion.getOptions();
                    if(options.isEmpty()) throw new RuntimeException("No options found");
                    char order = (char) (options.size() + 96);

                    System.out.println("\nWhat do you want to perform?");
                    System.out.println("1. Add option");
                    System.out.println("2. Update option");
                    System.out.println("3. Delete option");
                    int operation = InputUtil.getInt(1, 3);

                    // Add options block
                    if (operation == 1) {
                        while (true) {
                            System.out.println("Provide value for option(" + (char)(order+1) + "): ");
                            System.out.println("(enter 'Multiline' for multiple line option)");
                            String option = InputUtil.getString();
                            if(option.equalsIgnoreCase("multiline"))
                                options.add(getMultilineOption());
                            else
                                options.add(option);

                            System.out.println("Do you want to add more option?");
                            if(!InputUtil.getYesNo()) break;
                        }
                    }

                    // Option update block
                    else if (operation == 2) {
                        while (true) {
                            System.out.println("Which option [a-" + order + "] do you want to update");
                            char optionNumber = InputUtil.getChar('a', order);

                            System.out.print("Provide your new value for option(" + order + "): ");
                            System.out.println("(enter 'Multiline' for multiple line option)");
                            String option = InputUtil.getString();
                            if(option.equalsIgnoreCase("multiline"))
                                options.set(optionNumber - 97, getMultilineOption());
                            else
                                options.set(optionNumber - 97, option);
//
                            System.out.println("Do you want to update more option?");
                            if(!InputUtil.getYesNo()) break;
                        }
                    }

                    // Option delete block
                    else if (operation == 3) {
                        while (true) {
                            System.out.println("Which option [a-" + (order) + "] do you want to delete");
                            char option = InputUtil.getChar('a', order);
                            options.remove( option - 97);
                            System.out.println("Option '" + (option) + "' deleted");

                            System.out.println("\nDo you want to delete more option?");
                            if(!InputUtil.getYesNo()) break;
                        }
                    }
                    // updating the new options
                    actualQuestion.setOptions(options);

                    // for options confirmation
                    System.out.println("\nActual question after modification");
                    System.out.println(actualQuestion);
                }

                // updating the answer
                else if (choice == 3) {
                    char lastOption = (char) (actualQuestion.getOptions().size() + 96);
                    System.out.println("Insert your new(corrected) answer (option(a-"+ lastOption +"))");
                    char answer = InputUtil.getChar('a', lastOption);

                    Question updatedQuestion = questions.get(questionNumber - 1);
                    updatedQuestion.setAnswer(Character.toLowerCase(answer));

                    questions.set(questionNumber - 1, updatedQuestion);

                    // for answer confirmation
                    System.out.println("\nActual question after modification");
                    System.out.println(actualQuestion);
                }

                System.out.println("Do you want to update again in this question?");
                if(!InputUtil.getYesNo()) break;
            }

            System.out.println("\nDo you want to update more question?");
            if(!InputUtil.getYesNo()) break;
        }

        // pushing the corrected question to the same file
        FileUtil.putQuestionsToFile(nthFile, questions);
    }

    private static String getMultilineQuestion() {
        System.out.println();
        System.out.println("Enter/paste the question (enter 'END' on a new line to finish question)");
        return InputUtil.getPara() + '\n';
    }

    private static String getMultilineOption() {
        System.out.println();
        System.out.println("Enter/paste the option (enter 'END' on a new line to finish option)");
        return InputUtil.getPara() + '\n';
    }


}
