package controller;

import documents.entities.Client;
import documents.entities.Node;
import documents.entities.Packet;
import handlers.ClientHandler;
import handlers.NodeHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class Controller implements Serializable {

  private final int port;
  private ServerSocket serverSocket;

  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private volatile int clientId = 0;


  public static LinkedList<Node> nodes;
  public static LinkedList<Client> clients;
  public static HashMap<Integer,Node> clientsMapper;

  private static final Controller controller = new Controller();

  public static Controller getController() {
    return controller;
  }

  private Controller() {
    this.port = 8080;
    nodes = new LinkedList<>();
    clients = new LinkedList<>();
    clientsMapper = new HashMap<>();
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Server started");
    } catch (IOException e) {
      System.out.println("Error initializing controller");
    }
  }

  public void start() {
    while (!this.serverSocket.isClosed()) {
      try {
        Socket socket = serverSocket.accept();
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        Packet socketType = (Packet) objectInputStream.readObject();
        Thread socketHandler = null;
        if (socketType.getMessage().equals("nodeConnection"))
          socketHandler = new Thread(new NodeHandler(socket,objectInputStream,objectOutputStream));
        else if (socketType.getMessage().equals("clientConnection"))
          socketHandler = new Thread(new ClientHandler(objectInputStream,objectOutputStream,socket,clientId++));

        if (socketHandler != null) socketHandler.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public int getPort() {
    return port;
  }
}
