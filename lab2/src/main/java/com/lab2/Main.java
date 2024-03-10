package com.lab2;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        ResultManager resultManager = new ResultManager();
        List<Thread> threads = new LinkedList<>();

        int numThreads = args.length > 0 ? Integer.parseInt(args[0]) : scanner.nextInt();
        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(new CalculationThread(taskManager, resultManager));
            thread.start();
            threads.add(thread);
        }

        boolean running = true;
        while (running) {
            System.out.println("Wprowadz liczbe lub 'x' aby zakończyć lub 'p' aby wyświetlić wyniki");
            String input = scanner.next();
            if (input.equalsIgnoreCase("x")) {
                running = false;
                for (Thread thread : threads) {
                    thread.interrupt();
                }
                taskManager.shutdown();
            } else if (input.equalsIgnoreCase("p")) {
                System.out.println("Wyniki:");
                while (resultManager.hasResult()) {
                    System.out.println(resultManager.getResult());
                }
            }
            else {
                if (!input.matches("\\d+")) {
                    System.out.println("Nieprawidłowa komenda. Wprowadź liczbę lub 'x' aby zakończyć.");
                    continue;
                }
                taskManager.addTask(Integer.parseInt(input));
            }
        }
        scanner.close();
    }
}
