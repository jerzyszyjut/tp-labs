package com.lab2;

import java.util.LinkedList;

public class ResultManager {
    private final LinkedList<String> results = new LinkedList<>();

    public synchronized void addResult(String result) {
        results.add(result);
        //System.out.println(result);
    }

    public synchronized String getResult() {
        return results.remove();
    }

    public synchronized boolean hasResult() {
        return !results.isEmpty();
    }
}

