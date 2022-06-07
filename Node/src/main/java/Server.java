import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port;
    public static int load;
    private ServerSocket serverSocket;
    private Socket toController;

    public static Server server = new Server();

    public static Server getServer(){
        return server;
    }
    private Server()  {
        load = 0;
        try {
            this.serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            System.out.println("Error starting node");
            port = -1;
            return;
        }
        this.port = serverSocket.getLocalPort();
        System.out.println("Node loaded at port "+port);
    }
    public void startNode(){
        sendPortToController();
        while (!this.serverSocket.isClosed()){
            try {
                Socket client = serverSocket.accept();
                System.out.println("Accepted client");
                Thread clientThread = new Thread(new ClientHandler(client));
                clientThread.start();
            } catch (IOException e) {
                System.out.println("Error accepting clients, try rebooting this node");
            }
        }
    }
    public int getPort() {
        return port;
    }
    public void closeServer() {
        try{
            this.serverSocket.close();
            System.out.println("Closing server");
        }
        catch (Exception e){
            System.out.println("Error closing server");
        }
    }
    private void sendPortToController(){
        try {
            toController = new Socket("localhost",8080);
            DataOutputStream dataOutputStream = new DataOutputStream( toController.getOutputStream());
            dataOutputStream.writeInt(1);
            dataOutputStream.writeInt(port);
        } catch (IOException e) {
            System.out.println("Error giving port to controller, try again please");
        }
    }

}
