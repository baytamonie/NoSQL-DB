import entities.Node;

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
    private LinkedList<Node> nodes;

    private enum Sockets  {
        CLIENT(0),NODE(1);
        private final int identifier;
         Sockets(int identifier){
            this.identifier = identifier;
        }
        @Override
        public String toString(){
            return String.valueOf(identifier);
        }
    }


    public Controller(){
        this.port = 8080;
        nodes = new LinkedList<>();
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            System.out.println("Error initializing controller");
        }
    }

    public void start(){
        AtomicInteger i =new AtomicInteger(0);
        while(!this.serverSocket.isClosed()){
            try {
                Socket socket = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                int readInt = dataInputStream.readInt();
                Sockets socketType;

                if(readInt ==0)
                    socketType = Sockets.CLIENT;
                else if(readInt ==1)
                    socketType = Sockets.NODE;
                else continue;

                Thread socketHandler;
                switch (socketType) {
                    case NODE:
                        socketHandler = new Thread(
                                () -> {
                                    Node node = null;
                                    try {
                                        node = new Node(dataInputStream.readInt());
                                        System.out.println("Server received = " + node.getPort());
                                        nodes.add(node);
                                        while (!socket.isClosed()) {
                                            if (dataInputStream.read() == -1) {
                                                System.out.println("Client disconnected");
                                                socket.close();
                                                nodes.remove(node);
                                            }
                                        }
                                    } catch (IOException e) {
                                        nodes.remove(node);
                                        System.out.println("Node on port " + node.getPort() + " disconnected");
                                    }
                                });
                        break;
                    case CLIENT:
                        socketHandler = new Thread(
                                () -> {
                                    if (nodes.isEmpty()) {
                                        System.out.println("No available nodes right now, sorry try again later.");
                                    } else {
                                        try {
                                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                            dataOutputStream.writeInt(nodes.get(i.get() % nodes.size()).getPort());
                                            System.out.println("Client connected at node "+String.valueOf(i.getAndIncrement()%nodes.size()));


                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                        break;
                     default:
                         socketHandler = null;

                }
                if(socketHandler!=null)
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
