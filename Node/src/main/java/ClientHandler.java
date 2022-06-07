import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private  DataInputStream dataInputStream;


    public ClientHandler(Socket socket) {
        this.socket = socket;
        try{
            Server.load++;
            dataInputStream = new DataInputStream(socket.getInputStream());
        }
        catch (IOException ioException){
        System.out.println("Error handling client");
        }

    }

    @Override
    public void run() {
        while (!socket.isClosed()){
        try {
            switch (dataInputStream.readInt()) {

                case-1:
                    Server.load--;
                    System.out.println("Client disconnected");
                    socket.close();
                    break;
                case 1:

            }
            }
            catch(Exception e){
                Server.load--;
                System.out.println("Client disconnected");
                }


        }
    }
}
