package server;

import documents.IdsObject;
import documents.entities.Packet;
import documents.entities.User;
import documents.functions.DocumentFunction;
import documents.functions.DocumentFunctionsFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Server;
import utilities.DocumentUtils;
import utilities.FileHandler;

import javax.print.Doc;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable {

  private final Socket client;
  private final Socket controller;
  private ObjectInputStream objectInputStreamClient;
  private ObjectOutputStream objectOutputStreamController;
  private final DocumentFunctionsFactory documentFunctionsFactory;
  private boolean isLoggedIn;
  private User user;

  private final ObjectOutputStream objectOutputStreamClient;

  public ClientHandler(Socket client, Socket controller, ObjectOutputStream objectOutputStream) {
    this.client = client;
    this.controller = controller;
    this.isLoggedIn = false;
    this.documentFunctionsFactory = new DocumentFunctionsFactory();
    try {
      objectOutputStreamClient = new ObjectOutputStream(client.getOutputStream());
      objectInputStreamClient = new ObjectInputStream(client.getInputStream());
      objectOutputStreamController = objectOutputStream;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void run() {
    while (!client.isClosed()) {
      try {
       String command = ((Packet) objectInputStreamClient.readObject()).getMessage();
       DocumentFunction functionToExecute = documentFunctionsFactory.createDocumentFunction(command);
       boolean didFunctionExecute = functionToExecute.execute(objectInputStreamClient,objectOutputStreamClient);
       if(!didFunctionExecute){
         objectOutputStreamClient.writeObject(new Packet("-1"));
       }
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    try {
      objectOutputStreamController.writeObject(new Packet("load"));
      objectOutputStreamController.writeObject(new Packet(String.valueOf(Server.load--)));
      System.out.println("Client disconnected");
    } catch (Exception e) {
      System.out.println("Error sending load to server");
    }
  }
}
