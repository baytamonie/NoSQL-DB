package driver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnector {
    private DataInputStream fromServer;
    private ObjectOutputStream toServer;
    private Socket socket;
    private  int port;

    public ClientConnector(){
        try{
        this.port = getNodePort();
        if(port ==-1)
        {
            System.out.println("Error connecting client to port");
            return;
        }
        this.socket = new Socket("localhost",port);
        toServer = new ObjectOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
    }
    catch (Exception e){
        System.out.println("Error initializing client");
     }
    }
    public int getNodePort(){
        try {
            Socket connector = new Socket("localhost",8080);
            DataOutputStream dataOutputStream = new DataOutputStream(connector.getOutputStream());
            dataOutputStream.writeInt(0);
            DataInputStream inputStream = new DataInputStream(connector.getInputStream());
            int port = inputStream.readInt();
            return port;
        } catch (IOException e) {
            System.out.println("Error giving port");
            return -1;
        }

    }
}
