package controller;
import entities.Node;
import handlers.ClientHandler;
import handlers.NodeHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {

    private final int port;
    private ServerSocket serverSocket;
    private DataInputStream dataInputStream;

    public static LinkedList<Node> nodes;

    private final static Controller controller = new Controller();

    public static Controller getController(){
        return controller;
    }

    private Controller(){
        this.port = 8080;
        nodes = new LinkedList<>();
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            System.out.println("Error initializing controller");
        }
    }

    public void start(){
        while(!this.serverSocket.isClosed()){
            try {
                Socket socket = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                int readInt = dataInputStream.readInt();
                Thread socketHandler;
                switch (readInt){
                    case 1:
                        socketHandler = new Thread(new NodeHandler(socket,dataInputStream));
                        break;
                    case 0:
                        socketHandler = new Thread(new ClientHandler(socket));
                        break;
                    default: continue;
                }
                socketHandler.start();

                } catch (IOException e) {
                System.out.println("Error accepting node");
            }
        }
    }


    public int getPort() {
        return port;
    }
}
