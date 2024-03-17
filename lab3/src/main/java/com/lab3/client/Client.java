package com.lab3.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

import com.lab3.protocol.Message;
import com.lab3.protocol.Protocol;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);

            Socket socket = new Socket("127.0.1.1", 8080);
            LOGGER.info("Connected to server.");

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            String readySignal = (String) inputStream.readObject();

            if (!readySignal.equals(Protocol.READY)) {
                LOGGER.info("Server is not ready. Closing...");
                socket.close();
                return;
            }

            LOGGER.info("Enter a number:");
            int number = scanner.nextInt();
            outputStream.writeInt(number);
            outputStream.flush();

            String readyForMessagesSignal = (String) inputStream.readObject();
            
            if (!readyForMessagesSignal.equals(Protocol.READY_FOR_MESSAGES)) {
                LOGGER.info("Server is not ready for messages. Closing...");
                socket.close();
                return;
            }

            for (int i = 0; i < number; i++) {
                Message message = new Message(i, "PIWO WIADOMOSC NR " + i);
                outputStream.writeObject(message);
                outputStream.flush();
                LOGGER.info("Sent message to server: " + message.getMessage());
            }

            String finishedSignal = (String) inputStream.readObject();
            if (!finishedSignal.equals(Protocol.FINISHED)) {
                LOGGER.info("Server did not finish properly.");
            }

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            LOGGER.info("Client closed.");
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}
