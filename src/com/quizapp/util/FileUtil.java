package com.quizapp.util;

import com.quizapp.model.Question;
import com.quizapp.model.Score;
import com.quizapp.model.User;
import com.sun.source.util.Trees;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class FileUtil {

    public static boolean validateUserName(String userName) {
        ArrayList<User> users = readObjectFromFile(new File("resources/users.dat"));

        for (User user : users) {
            if (user.getUserName().equals(userName))
                return true;
        }
        return false;
    }

    public static boolean validateUser(String userName, String password) {
        ArrayList<User> users = readObjectFromFile(new File("resources/users.dat"));

        for (User user : users) {
            if (userName.equals(user.getUserName()) && user.getPassword().equals(password))
                return true;
        }
        return false;
    }

    public static boolean updateUserName(String oldName, String newName, String password) {
        File file = new File("resources/users.dat");
        ArrayList<User> users = readObjectFromFile(file);
        for (User user : users) {
            if (oldName.equals(user.getUserName()) && user.getPassword().equals(password)) {
                user.setUserName(newName);
                writeObjectToFile(file, users);
                updateUserNameOnScoreFile(oldName, newName);
                return true;
            }
        }
        return false;
    }

    public static void updateUserNameOnScoreFile(String oldName, String newName) {
        File scoreFile = new File("resources/scores.dat");
        ArrayList<Score> scores = readObjectFromFile(scoreFile);
        for (Score score : scores) {
            if(score.getName().equals(oldName)) {
                score.setName(newName);
            }
        }
        writeObjectToFile(scoreFile, scores);
    }

    public static boolean updatePassword(String userName, String oldPassword, String newPassword) {
        File file = new File("resources/users.dat");
        ArrayList<User> users = readObjectFromFile(file);
        for (User user : users) {
            if (userName.equals(user.getUserName()) && user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                writeObjectToFile(file, users);
                return true;
            }
        }
        return false;
    }

    public static boolean createQuizFile(String fileName) {
        File file = new File("resources/questions/" + fileName + ".txt");
        try {
            if (file.exists()) {
                System.out.println("File already exists!");
                return false;
            }
            if(file.createNewFile())
                System.out.println("File created successfully");
            else System.out.println("New file not created");

        } catch (IOException e) {
            System.out.println("Something went wrong while creating file");
            System.exit(0);
        }
        return true;
    }

    public static void addQuestion(ArrayList<Question> questions, String fileName) {
        try (
            FileOutputStream fos = new FileOutputStream("resources/questions/" + fileName + ".txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(questions);
            oos.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void appendQuestion(int nthFile, ArrayList<Question> questions) {

        // getting questions presented folder
        File folder = new File("resources/questions");

        File[] files = folder.listFiles();
        if(files == null) {
            System.out.println("No files found");
            return;
        }
        File file = files[nthFile - 1]; // why -1? => 0 index based

        // reading the existing questions, for appending at the end
        ArrayList<Question> existingQuestions = readObjectFromFile(file);

        existingQuestions.addAll(questions); // adding new questions to the existing

        writeObjectToFile(file, existingQuestions); // pushing it to the same file
    }

    public static <T> ArrayList<T> readObjectFromFile(File file) {
        try (
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            return (ArrayList<T>) ois.readObject();

        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
            throw new RuntimeException("Source file not found to read");
        }
    }

    public static <T> void writeObjectToFile(File file, ArrayList<T> objects) {
        try (
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(objects);
            oos.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteQuestion(int nthFile) {

        // fetching the questions from the file
        File folder = new File("resources/questions");
        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println("Empty folder");
            return;
        }
        ArrayList<Question> questions = readObjectFromFile(files[nthFile - 1]);

        // deleting the questions
        while (true) {
            int questionsCount = readFile(nthFile);
            if (questionsCount == 0) {
                System.out.println("Empty file");
                return;
            }
            System.out.println("\nWhich question do you want to delete? (0-skip)");
            int questionNumber = InputUtil.getInt(0, questionsCount);

            if (questionNumber == 0) break;

            questions.remove(questionNumber - 1);
            System.out.println("Question number " + questionNumber + " removed\n");

            System.out.println("Do you want to delete more question?");
            System.out.println("(1. Yes, 2. No)");
            int option = InputUtil.getInt(1, 2);

            if(option == 2) break;
        }

        // pushing the remaining question to the same file
        writeObjectToFile(files[nthFile - 1], questions);
    }

    public static void updateQuestion(int nthFile) {

        // fetching the questions from the file
        File folder = new File("resources/questions");
        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println("Empty folder");
            return;
        }
        ArrayList<Question> questions = readObjectFromFile(files[nthFile - 1]);

        // updating the questions
        while (true) {
            int questionsCount = readFile(nthFile);

            if (questionsCount == 0) {
                System.out.println("Empty file");
                return;
            }

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
                    System.out.println("(enter 'Multiline' for multiple line entries)");
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
                    char order = (char) (options.size() + 96);

                    System.out.println("\nWhat do you want to perform?");
                    System.out.println("1. Add option");
                    System.out.println("2. Update option");
                    System.out.println("3. Delete option");
                    int operation = InputUtil.getInt(1, 3);

                    // Add options block
                    if (operation == 1) {
                        while (true) {
                            System.out.println("Provide value for option(" + order + "): ");
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
                            System.out.println("Which option [a-" + (char)(actualQuestion.getOptions().size()+96) + "] do you want to update");
                            int optionNumber = InputUtil.getInt(1, order - 96);

                            System.out.print("Provide your new value for option(" + (char)(optionNumber + 96) + "): ");
                            System.out.println("(enter 'Multiline' for multiple line option)");
                            String option = InputUtil.getString();
                            if(option.equalsIgnoreCase("multiline"))
                                options.set(optionNumber - 1, getMultilineOption());
                            else
                                options.set(optionNumber - 1, option);
//
                            System.out.println("Do you want to update more option?");
                            if(!InputUtil.getYesNo()) break;
                        }
                    }

                    // Option delete block
                    else if (operation == 3) {
                        while (true) {
                            System.out.println("Which option [a-" + (char)(actualQuestion.getOptions().size()+96) + "] do you want to delete");
                            int option = InputUtil.getInt(1, order - 96);
                            options.remove( option - 1);
                            System.out.println("Option '" + (char)(option + 96) + "' deleted");

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
                    System.out.println("Insert your new(corrected) answer (option(a-"+ lastOption +")");
                    char answer = InputUtil.getChar('a', lastOption);

                    Question updatedQuestion = questions.get(questionNumber - 1);
                    updatedQuestion.setAnswer(answer);

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
        writeObjectToFile(files[nthFile - 1], questions);

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

    private static String getMultilineAnswer() {
        System.out.println();
        System.out.println("Enter/paste the answer (enter 'END' on a new line to finish answer)");
        return InputUtil.getPara() + '\n';
    }

    // it returns the number of files present
    public static int showQuizTypes() {
        File folder = new File("resources/questions");
        String[] files = folder.list();

        int order = 0;
        if (files == null) return order;
        for (String file : files) {
            System.out.println(++order + ". " + file.substring(0, file.length() - 4)); // shows only name
        }
        return order;
    }

    public static ArrayList<String> getQuizTypes() {
        File folder = new File("resources/questions");
        String[] files = folder.list();
        if(files == null) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(files));
    }

    public static String getQuizName(int nthFile) {
        File folder = new File("resources/questions");
        String[] files = folder.list();
        if (files == null) return "";
        return files[nthFile - 1].substring(0, files[nthFile - 1].length() - 4);
    }

    public static ArrayList<Question> getQuestions(int nthFile) {
        File folder = new File("resources/questions");
        File[] files = folder.listFiles();
        if (files == null) {
            System.out.println("No questions found");
            return null;
        }
        return readObjectFromFile(files[nthFile - 1]);
    }

    public static int readFile(int nthFile) {
        File folder = new File("resources/questions");
        File[] files = folder.listFiles();

        if(files == null) {
            System.out.println("No quiz files found");
            return 0;
        }
        ArrayList<Question> questions = readObjectFromFile(files[nthFile - 1]);

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
        return questions.size();
    }

    public static void writeUserToFile(String userName, String password) {
        File file = new File("resources/users.dat");
        ArrayList<User> users = readObjectFromFile(file); // reading the existing users from user.txt
        users.add(new User(userName, password));  // adding new user
        writeObjectToFile(file, users);  // push the users data to the same file
    }

    public static void writeScore(Score newScore) {
        File file = new File("resources/scores.dat");
        ArrayList<Score> scores = readObjectFromFile(file);
        scores.add(newScore);
        writeObjectToFile(file, scores);
    }

    public static ArrayList<Score> getScore() {

        ArrayList<Score> scores = new ArrayList<>();
        try (
                FileInputStream fin = new FileInputStream("resources/scores.dat");
                ObjectInputStream ois = new ObjectInputStream(fin);
        ) {
            scores = (ArrayList<Score>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("No scores found");
        }
        return scores;
    }

    public static TreeSet<String> getUsers() {
        File file = new File("resources/users.dat");
        ArrayList<User> users = readObjectFromFile(file);
        TreeSet<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for(User user : users)
            names.add(user.getUserName());
        return names;
    }
}
