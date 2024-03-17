package com.lab3.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

import com.lab3.protocol.Message;
import com.lab3.protocol.Protocol;

public class ServerThread implements Runnable {
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    Logger LOGGER;

    public ServerThread(ObjectOutputStream outputStream, ObjectInputStream inputStream, Logger LOGGER) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.LOGGER = LOGGER;
    }

    @Override
    public void run() {
        try {
            outputStream.writeObject(Protocol.READY);

            int n = inputStream.readInt();
            LOGGER.info("Received number from client: " + n);

            outputStream.writeObject(Protocol.READY_FOR_MESSAGES);

            for (int i = 0; i < n; i++) {
                Message message = (Message) inputStream.readObject();
                LOGGER.info("Received message from client: " + message.getMessage());
            }

            outputStream.writeObject(Protocol.FINISHED);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
