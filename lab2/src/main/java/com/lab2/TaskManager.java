package com.lab2;

import java.util.LinkedList;

public class TaskManager {
    private final LinkedList<Integer> numberToChecks = new LinkedList<>();

    public synchronized void addTask(int numberToCheck) {
        numberToChecks.add(numberToCheck);
        notify();
    }

    public synchronized int getTask() throws InterruptedException {
        while (numberToChecks.isEmpty()) {
            wait();
        }
        return numberToChecks.remove();
    }

    public synchronized void shutdown() {
        notifyAll();
    }
}
