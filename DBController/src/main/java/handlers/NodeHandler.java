package handlers;
import controller.Controller;
import entities.Node;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
public class NodeHandler implements Runnable{

    private final Socket socket;
    private final DataInputStream dataInputStream;



    public NodeHandler(Socket socket, DataInputStream dataInputStream) {
        this.socket = socket;
        this.dataInputStream = dataInputStream;
    }

    @Override
    public void run() {
        Node node = null;
        try {
            node = new Node(dataInputStream.readInt());
            System.out.println("Node received. Node is at port: "+node.getPort());
            Controller.nodes.add(node);
            while(!socket.isClosed()){
                switch (dataInputStream.readInt()){
                    case -1:
                        System.out.println("Node at port: "+ node.getPort()+" disconnected");
                        socket.close();
                        Controller.nodes.remove(node);
                        break;
        }
            }

        } catch (IOException e) {
            System.out.println("Node at port: "+ node.getPort()+" disconnected");
            try {
                socket.close();
            } catch (IOException ex) {
            System.out.println("error closing socket");
            }
            Controller.nodes.remove(node);
        }

    }
}
