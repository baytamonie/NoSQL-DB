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

  public ClientHandler(Socket client, Socket controller) {
    this.client = client;
    this.controller = controller;
    this.isLoggedIn = false;
  }

  public void login() throws IOException, ClassNotFoundException {

    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream = new ObjectInputStream(controller.getInputStream());
    Packet packet = (Packet) objectInputStream.readObject();
    String username = packet.getMessage();
    packet = (Packet) objectInputStream.readObject();
    String password = packet.getMessage();
    objectOutputStream = new ObjectOutputStream(client.getOutputStream());
    for (User user : Server.userList) {
      if (user.getName().equals(username) && user.getPassword().equals(password)) {
        objectOutputStream.writeObject(new Packet("true"));
        System.out.println("successful login");
        objectOutputStream.writeObject(new Packet(user.getAuthority()));
        objectOutputStream.close();
        objectInputStream.close();
        return;
      }
    }
    objectOutputStream.writeObject(new Packet("false"));
    System.out.println("not successful login");
    objectOutputStream.close();
    objectInputStream.close();
  }

  @Override
  public void run() {
    while (!client.isClosed()) {
      Packet command;
      try {
        ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
        command = (Packet) objectInputStream.readObject();
        switch (command.getMessage().toLowerCase()) {
          case "login":
            System.out.println(command.getMessage());
            login();
            break;
          case "readById":
            break;
        }
        objectInputStream.close();
      } catch (IOException e) {
        System.out.println("error creating input stream with client");
      } catch (ClassNotFoundException e) {
        System.out.println("error reading object from client");
      }
    }
  }
}
