package server;

import databaseTransfer.HorizontalScaling;
import documents.entities.Packet;
import documents.entities.User;
import org.json.simple.JSONObject;
import utilities.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {

  private final int port;
  public static volatile int load;
  private ServerSocket serverSocket;
  private Socket controller;
  private ObjectOutputStream objectOutputStreamController;

  private final Scanner scanner;

  public static Server server = new Server();
  public static List<User> userList;
  private static final String usersPath = "src/main/resources/databases/usernames.json";
  private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
  private ObjectInputStream objectInputStreamController;
  private final FileUtils fileUtils = FileUtils.getInstance();

  private void loadUsers() {
    List<JSONObject> usersJSON = fileUtils.loadData(usersPath);
    for (JSONObject userJSON : usersJSON) {
      User user =
          new User(
              (String) userJSON.get("username"),
              (String) userJSON.get("password"),
              (String) userJSON.get("permissions"));
      userList.add(user);
    }
  }

  public static Server getServer() {
    return server;
  }

  private Server() {
    load = 0;
    scanner = new Scanner(System.in);
    userList = new LinkedList<>();
    try {
      this.serverSocket = new ServerSocket(0);
    } catch (IOException e) {
      System.out.println("Error starting node");
      port = -1;
      return;
    }
    this.port = serverSocket.getLocalPort();
    System.out.println("Node loaded at port " + port);
  }

  public void startNode() {
    sendPortToController();
    HorizontalScaling horizontalScaling =
        new HorizontalScaling(objectInputStreamController, objectOutputStreamController,controller);
    loadUsers();
    Thread checkUserInput =
        new Thread(
            () -> {
              if (scanner.next().equals("STOP")) this.closeServer();
            });
    Thread scaling = new Thread(horizontalScaling);
    scaling.start();
    checkUserInput.start();

    while (!this.serverSocket.isClosed()) {
      try {
        Socket client = serverSocket.accept();
        reentrantReadWriteLock.writeLock().lock();
        load++;
        reentrantReadWriteLock.writeLock().unlock();
        System.out.println("Accepted client");
        Thread clientThread =
            new Thread(
                new ClientHandler(
                    client));

        clientThread.start();

      } catch (IOException e) {
        System.out.println("Error accepting clients, try rebooting this node");
      }
    }
  }


  public void closeServer() {
    try {
      this.serverSocket.close();
      System.out.println("Closing server");
    } catch (Exception e) {
      System.out.println("Error closing server");
    }
  }

  private void sendPortToController() {
    try {
      controller = new Socket("localhost", 8080);
      objectOutputStreamController = new ObjectOutputStream(controller.getOutputStream());
      objectInputStreamController = new ObjectInputStream(controller.getInputStream());

      Packet controllerConnection = new Packet("nodeConnection");
      Packet sendPortNumber = new Packet(String.valueOf(port));
      objectOutputStreamController.writeObject(controllerConnection);
      objectOutputStreamController.writeObject(sendPortNumber);

    } catch (IOException e) {
      System.out.println("Error giving port to controller, try again please");
      throw new RuntimeException();
    }
  }
}
