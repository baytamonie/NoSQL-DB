package handlers;

import controller.Controller;
import documents.entities.Packet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket socket;


    public ClientHandler(Socket socket ) {
        this.socket = socket;
    }

    @Override
    public void run() {
        if( Controller.nodes.isEmpty()){
            System.out.println("No available nodes right now to serve the client");
        }
        else{
            try{
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(new Packet(String.valueOf(Controller.nodes.get(0).getPort())));
                System.out.println("Client connected at node "+(Controller.nodes.get(0).getPort()));
                objectOutputStream.close();;
            }
            catch (IOException e){
                System.out.println("Error connecting client to node");
            }
        }

    }
}
