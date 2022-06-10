import documents.entities.Packet;
import documents.entities.User;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

  private final Socket client;
  private final Socket controller;
  private DataInputStream dataInputStreamClient;
  private ObjectInputStream objectInputStreamClient;
  private ObjectOutputStream objectOutputStreamController;

  private boolean isLoggedIn;
  private User user;

  private final ObjectOutputStream objectOutputStreamClient;
  public ClientHandler(Socket client, Socket controller) {
    this.client = client;
    this.controller = controller;
    this.isLoggedIn = false;
    try {
      objectOutputStreamClient = new ObjectOutputStream(client.getOutputStream());
      objectInputStreamClient = new ObjectInputStream(client.getInputStream());
      objectOutputStreamController = new ObjectOutputStream(controller.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


  }

  public void login() throws IOException, ClassNotFoundException {


    Packet packet = (Packet) objectInputStreamClient.readObject();
    String username = packet.getMessage();
    packet = (Packet) objectInputStreamClient.readObject();
    String password = packet.getMessage();
    for (User user : Server.userList) {
      if (user.getName().equals(username) && user.getPassword().equals(password)) {
        objectOutputStreamClient.writeObject(new Packet("true"));
        System.out.println("successful login");
        objectOutputStreamClient.writeObject(new Packet(user.getAuthority()));
        return;
      }
    }
    objectOutputStreamClient.writeObject(new Packet("false"));
    System.out.println("not successful login");

  }

  @Override
  public void run() {
    while (!client.isClosed()) {

      Packet command;
      try {
        command = (Packet) objectInputStreamClient.readObject();
        System.out.println(command);
        switch (command.getMessage().toLowerCase()) {
          case "login":
            login();
            break;
          case "readById":
            break;
        }
      } catch (IOException e) {
        try {
          objectOutputStreamController.writeObject(new Packet("load"));
          client.close();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
        System.out.println("client disconnected");
        Server.load--;
        try {
          objectOutputStreamController.writeObject(String.valueOf(Server.load));
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
        System.out.println("error creating input stream with client");
        try {
          client.close();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      } catch (ClassNotFoundException e) {
        System.out.println("error reading object from client");
      }
    }
  }
}
