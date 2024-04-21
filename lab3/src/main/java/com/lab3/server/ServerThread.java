package com.lab3.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import com.lab3.protocol.Message;
import com.lab3.protocol.Protocol;

public class ServerThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            outputStream.writeObject(Protocol.READY);

            // int n = inputStream.readInt();
            // LOGGER.info("Received number from client: " + n);

            outputStream.writeObject(Protocol.READY_FOR_MESSAGES);
            boolean flag = true;

            while(flag) {
                Message message = (Message) inputStream.readObject();
                LOGGER.info("Received message from client: " + message.getMessage());
                outputStream.writeObject(message);
                outputStream.flush();
                LOGGER.info("Sent message to client: " + message.getMessage());
                if(message.getMessage().toString().equals("exit")) flag = false;
            }

            outputStream.writeObject(Protocol.FINISHED);

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
