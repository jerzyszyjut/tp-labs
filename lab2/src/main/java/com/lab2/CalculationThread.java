package com.lab2;

public class CalculationThread implements Runnable {
    private final TaskManager taskManager;
    private final ResultManager resultManager;

    public CalculationThread(TaskManager taskManager, ResultManager resultManager) {
        this.taskManager = taskManager;
        this.resultManager = resultManager;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            try {
                int number = taskManager.getTask();
                boolean isPrime = true;
                for (int i = 2; i < number; i++) {
                    if (number % i == 0) {
                        isPrime = false;
                        break;
                    }
                }
                Thread.sleep(10000);
                String result = number + " " + (isPrime ? "jest" : "nie jest") + " liczbą pierwszą";
                resultManager.addResult(result);
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}
