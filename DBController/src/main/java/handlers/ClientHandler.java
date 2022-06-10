package handlers;

import controller.Controller;
import documents.entities.Packet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable{

    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;

    public ClientHandler(Socket socket, ObjectOutputStream objectOutputStream) {
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {
        if( Controller.nodes.isEmpty()){
            System.out.println("No available nodes right now to serve the client");
        }
        else{
            try{
                objectOutputStream.writeObject(new Packet(String.valueOf(Controller.nodes.get(0).getPort())));
                Controller.nodes.stream().sorted().collect(Collectors.toList());
                System.out.println("Client connected at node "+(Controller.nodes.get(0).getPort()));
            }
            catch (IOException e){
                System.out.println("Error connecting client to node");
            }
        }

    }
}
