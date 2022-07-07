package driver;


import documents.entities.Packet;

import java.io.*;
import java.net.Socket;

public class ClientConnector {

    private  ObjectInputStream objectInputStream;
    private  ObjectOutputStream objectOutputStream;


    public ObjectInputStream getControllerInputStream(){
        return this.objectInputStream;
    }

    public ObjectOutputStream getControllerOutputStream(){
        return this.objectOutputStream;
    }
    public Socket initConnection() {
        try {
            int port = getNodePort();
            if (port == -1) {
                System.out.println("Error connecting client to any of the nodes");
                ClientDriver.connectionEstablished = false;
                return null;
            }
            return new Socket("localhost", port);
        } catch (Exception e) {
            ClientDriver.connectionEstablished = false;
            System.out.println("Error initializing client");
            throw new RuntimeException();
        }
    }

    public int getNodePort() {
        try {
            Socket connector = new Socket("localhost", 8080);
            objectOutputStream = new ObjectOutputStream(connector.getOutputStream());
            objectOutputStream.writeObject(new Packet("clientConnection"));
             objectInputStream = new ObjectInputStream(connector.getInputStream());
            Packet portNum = (Packet) objectInputStream.readObject();
            int port = Integer.parseInt(portNum.getMessage());
            System.out.println("node port: "+port);
            return port;
        } catch (Exception e) {
            System.out.println("Error sending port to Controller");
            ClientDriver.connectionEstablished = false;
            return -1;
        }

    }
}
