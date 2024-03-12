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
        while (!Thread.currentThread().isInterrupted()) {
            long number = 0;
            try {
                number = taskManager.getTask();
                System.out.println(number);
                StringBuilder sb = new StringBuilder();
                sb.append(number).append(": ");
                for (long i = 1; i < number; i++) {
                    if (number % i == 0) {
                        sb.append(i).append(", ");
                    }
                }
                sb.append(number);
                resultManager.addResult(sb.toString());
            } catch (InterruptedException e) {
                StringBuilder sb = new StringBuilder();
                if(number != 0) sb.append("Watek zostal przerwany, ostatnia liczba: ").append(number);
                else sb.append("Watek zostal przerwany");
                System.out.println(sb.toString());
                break;
            }
        }
    }
}
