package com.lab2;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        ResultManager resultManager = new ResultManager();
        List<Thread> threads = new LinkedList<>();

        // https://www.w3schools.com/java/java_files_read.asp
        try {
            File myObj = new File("test 1 watki.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                long number = Long.parseLong(data);
                taskManager.addTask(number);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter fw = new FileWriter("wyniki.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numThreads = args.length > 0 ? Integer.parseInt(args[0]) : scanner.nextInt();
        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(new CalculationThread(taskManager, resultManager));
            thread.start();
            threads.add(thread);
        }

        boolean running = true;
        while (running) {
            System.out.println("Wprowadz liczbe lub 'exit'");
            String input = scanner.next();
            if (input.equalsIgnoreCase("exit")) {
                running = false;
                for (Thread thread : threads) {
                    thread.interrupt();
                }
                taskManager.shutdown();
            }
            else {
                if (!input.matches("\\d+")) {
                    System.out.println("Nieprawidlowa komenda. Wprowadz liczbe lub 'exit' aby zakonczyc.");
                    continue;
                }
                taskManager.addTask(Long.parseLong(input));
            }
        }
        scanner.close();
    }
}
