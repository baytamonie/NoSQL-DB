import driver.ClientConnector;
import driver.ClientHandler;

public class Client {

    public static void main(String[] args){

        ClientHandler clientHandler = new ClientHandler();
        clientHandler.createConnection();
        clientHandler.login("bayta","123");


    }

}
