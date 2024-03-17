package com.lab3.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        Thread thread = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
            LOGGER.info("Server started. Waiting for clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                LOGGER.info("Client connected: " + socket.getInetAddress());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

                thread = new Thread(new ServerThread(outputStream, inputStream, LOGGER));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (thread != null) {
                thread.interrupt();
            }
        }
    }
}
