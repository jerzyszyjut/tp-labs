package com.lab2;

import java.util.LinkedList;

public class TaskManager {
    private final LinkedList<Long> numberToChecks = new LinkedList<>();

    public synchronized void addTask(long numberToCheck) {
        numberToChecks.add(numberToCheck);
        notify();
    }

    public synchronized long getTask() throws InterruptedException {
        while (numberToChecks.isEmpty()) {
            wait();
        }
        return numberToChecks.remove();
    }

    public synchronized void shutdown() {
        notifyAll();
    }
}
