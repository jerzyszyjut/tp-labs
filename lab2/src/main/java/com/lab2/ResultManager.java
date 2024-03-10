package com.lab2;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class ResultManager {
    private final ReentrantLock lock = new ReentrantLock();
    private final LinkedList<String> results = new LinkedList<>();

    public synchronized void addResult(String result) {
        lock.lock();
        results.add(result);
        lock.unlock();
        //System.out.println(result);
    }

    public synchronized String getResult() {
        lock.lock();
        String result = results.remove();
        lock.unlock();
        return result;
    }

    public synchronized boolean hasResult() {
        lock.lock();
        boolean result = !results.isEmpty();
        lock.unlock();
        return result;
    }
}

