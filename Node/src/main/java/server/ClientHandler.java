package server;

import documents.entities.Packet;
import documents.entities.User;
import documents.functions.DocumentReadFunctions;
import documents.functions.DocumentReadFunctionsFactory;
import documents.functions.DocumentWriteFunctions;
import documents.functions.DocumentWriteFunctionsFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

  private final Socket client;
  private final Socket controller;
  private ObjectInputStream objectInputStreamClient;
  private ObjectOutputStream objectOutputStreamController;
  private final DocumentReadFunctionsFactory documentReadFunctionsFactory;
  private final DocumentWriteFunctionsFactory documentWriteFunctionsFactory;

  private boolean isLoggedIn;
  private User user;

  private  ObjectOutputStream objectOutputStreamClient;
  private  ObjectInputStream objectInputStreamController;

  public ClientHandler(Socket client, Socket controller, ObjectOutputStream objectOutputStream,ObjectInputStream objectInputStream) {
    this.client = client;
    this.controller = controller;
    this.isLoggedIn = false;
    this.documentReadFunctionsFactory = new DocumentReadFunctionsFactory();
    this.documentWriteFunctionsFactory = new DocumentWriteFunctionsFactory();


    try {
      objectOutputStreamClient = new ObjectOutputStream(client.getOutputStream());
      objectInputStreamClient = new ObjectInputStream(client.getInputStream());
      objectOutputStreamController = objectOutputStream;
      objectInputStreamController = objectInputStream;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void run() {
    while (!client.isClosed()) {
      try {

       String command = ((Packet) objectInputStreamClient.readObject()).getMessage();
        System.out.println(command);
       if(command.equals("createDatabase") ||command.equals("createCollection")||command.equals("addDocument")){
        DocumentWriteFunctions functionToExecute = documentWriteFunctionsFactory.getFunction(command);
        functionToExecute.execute(objectInputStreamClient,objectOutputStreamClient,objectInputStreamController,objectOutputStreamController);
         continue;
       }
       DocumentReadFunctions functionToExecute = documentReadFunctionsFactory.createDocumentFunction(command);
       boolean didFunctionExecute = functionToExecute.execute(objectInputStreamClient,objectOutputStreamClient);
       if(!didFunctionExecute){
         objectOutputStreamClient.writeObject(null);
       }
//        DocumentWriteFunctions functionToExecute = documentWriteFunctionsFactory.getFunction(command);
//        System.out.println(functionToExecute);
//        functionToExecute.execute(objectInputStreamClient,objectOutputStreamClient,objectInputStreamController,objectOutputStreamController);
      } catch (IOException e) {
        try {
          client.close();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
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
