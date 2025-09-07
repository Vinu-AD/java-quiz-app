package com.quizapp.util;

import com.quizapp.model.Question;
import com.quizapp.model.Score;
import com.quizapp.model.User;

import java.io.*;
import java.util.*;

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

    public static TreeSet<String> getUsers() {
        File file = new File("resources/users.dat");
        ArrayList<User> users = readObjectFromFile(file);
        TreeSet<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for(User user : users)
            names.add(user.getUserName());
        return names;
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

    public static void addQuestions(String fileName, ArrayList<Question> questions) {
        File file = new File("resources/questions" + File.separator + fileName + ".txt");
        FileUtil.writeObjectToFile(file, questions);      // storing it in the file
    }

    public static ArrayList<Question> getQuestionsFromFile(int nthFile) {
        // fetching the questions from the file
        File folder = new File("resources/questions");
        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println("Empty folder");
            return new ArrayList<>();
        }
        return FileUtil.readObjectFromFile(files[nthFile - 1]);
    }

    public static void putQuestionsToFile(int nthFile, ArrayList<Question> questions) {
        // fetching the questions from the file
        File folder = new File("resources/questions");
        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println("Empty folder");
            return;
        }
        writeObjectToFile(files[nthFile - 1], questions);
    }

    public static <T> ArrayList<T> readObjectFromFile(File file) {
        try (
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (ArrayList<T>) ois.readObject();

        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
//            System.out.println(e);
            throw new RuntimeException("Source file not found to read");
        }
    }

    public static <T> void writeObjectToFile(File file, ArrayList<T> objects) {
        try (
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(objects);
            oos.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public static void readQuestionsFromFile(int nthFile) {
        File folder = new File("resources/questions");
        File[] files = folder.listFiles();

        if(files == null) {
            System.out.println("No quiz files found");
            return;
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
                ObjectInputStream ois = new ObjectInputStream(fin)
        ) {
            scores = (ArrayList<Score>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("No scores found");
        }
        return scores;
    }

}
