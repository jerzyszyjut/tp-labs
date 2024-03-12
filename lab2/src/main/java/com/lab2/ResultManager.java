package com.lab2;

import java.io.*;
import java.util.LinkedList;

public class ResultManager {
    private final LinkedList<String> results = new LinkedList<>();

    public synchronized void addResult(String result) {
        results.add(result);
        FileWriter fw = null;
        try {
            fw = new FileWriter("wyniki.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(result);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String getResult() {
        return results.remove();
    }

    public synchronized boolean hasResult() {
        return !results.isEmpty();
    }
}

