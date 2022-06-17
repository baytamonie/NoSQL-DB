package driver;

import documents.entities.Packet;

import java.io.*;
import java.net.Socket;

public class ClientConnector {
    private DataInputStream fromNode;
    private ObjectOutputStream toNode;
    private Socket socket;
    private int port;

    public Socket initConnection() {
        try {
            this.port = getNodePort();
            if (port == -1) {
                System.out.println("Error connecting client to port");
                ClientHandler.connectionEstablished = false;
                return null;
            }
            this.socket = new Socket("localhost", port);
            return socket;
        } catch (Exception e) {
            ClientHandler.connectionEstablished = false;
            System.out.println("Error initializing client");
            throw new RuntimeException();
        }
    }

    public int getNodePort() {
        try {
            Socket connector = new Socket("localhost", 8080);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(connector.getOutputStream());
            objectOutputStream.writeObject(new Packet("clientConnection"));
            ObjectInputStream objectInputStream = new ObjectInputStream(connector.getInputStream());
            Packet portNum = (Packet) objectInputStream.readObject();
            int port = Integer.valueOf(portNum.getMessage());
            System.out.println(port);
            return port;
        } catch (Exception e) {
            System.out.println("Error giving port");
            ClientHandler.connectionEstablished = false;
            return -1;
        }

    }
}
