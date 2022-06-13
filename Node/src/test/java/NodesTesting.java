import org.junit.Test;
import server.Server;

import java.util.Scanner;

public class NodesTesting {


    @Test
    public void checkIfNodeConnects(){
        Server server = Server.getServer();
        System.out.println("To stop server, type STOP");
        Scanner scanner = new Scanner(System.in);
        while(scanner.next()!="STOP")
            continue;
        server.startNode();
        server.closeServer();


    }


}
