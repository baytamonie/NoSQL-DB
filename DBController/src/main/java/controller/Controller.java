package controller;

import documents.entities.Node;
import documents.entities.Packet;
import handlers.ClientHandler;
import handlers.NodeHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Controller implements Serializable {

  private final int port;
  private ServerSocket serverSocket;
  private DataInputStream dataInputStream;
  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;

  public static LinkedList<Node> nodes;

  private static final Controller controller = new Controller();

  public static Controller getController() {
    return controller;
  }

  private Controller() {
    this.port = 8080;
    nodes = new LinkedList<>();
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
        System.out.println(socketType.getMessage());
        Thread socketHandler = null;
        if (socketType.getMessage().equals("nodeConnection"))
          socketHandler = new Thread(new NodeHandler(socket));
        else if (socketType.getMessage().equals("clientConnection"))
          socketHandler = new Thread(new ClientHandler(socket));
        objectInputStream.close();
        objectOutputStream.close();
        if (socketHandler != null) socketHandler.start();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public int getPort() {
    return port;
  }
}
