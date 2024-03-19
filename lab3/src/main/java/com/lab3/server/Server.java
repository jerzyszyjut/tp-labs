package com.lab3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final LinkedList<Thread> threads = new LinkedList<>();

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Scanner scanner = new Scanner(System.in);
        try {
            serverSocket = new ServerSocket(8080);
            LOGGER.info("Server started. Input 'exit' to stop the server.");
            LOGGER.info("Server is listening on port 8080.");
            LOGGER.info("Server is waiting for a client to connect...");

            while (true) {
                Socket socket = serverSocket.accept();
                LOGGER.info("Client connected: " + socket.getInetAddress());
                Thread thread = new Thread(new ServerThread(socket));
                thread.start();
                threads.add(thread);

                // Can be only closed after some client connects because serverSocket.accept() is blocking while loop
                if (System.in.available() > 0) {
                    String input = scanner.nextLine();
                    if (input.equals("exit")) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            LOGGER.info("Server is shutting down...");
            LOGGER.info("Closing all client connections...");
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LOGGER.info("All client connections closed.");
            LOGGER.info("Closing server socket...");
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            LOGGER.info("Server socket closed.");
            LOGGER.info("Server stopped.");
            scanner.close();
        }
    }
}
