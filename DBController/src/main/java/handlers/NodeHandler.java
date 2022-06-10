package handlers;
import controller.Controller;
import documents.entities.Node;
import documents.entities.Packet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class NodeHandler implements Runnable{

    private final Socket socket;
    private  ObjectInputStream objectInputStream;

  public NodeHandler(Socket socket) {
    this.socket = socket;
        }


    @Override
    public void run() {
        Node node = null;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Packet portNumberPacket = (Packet) objectInputStream.readObject();
            node = new Node(Integer.valueOf(portNumberPacket.getMessage()));
            System.out.println("Node received. Node is at port: "+node.getPort());
            Controller.nodes.add(node);
            objectInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
