

import server.Server;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Server server = Server.getServer();
        System.out.println("To stop server, type STOP");
        server.startNode();


    }

}
