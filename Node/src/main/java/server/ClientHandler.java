package server;

import documents.entities.Packet;
import documents.entities.User;
import documents.functions.DocumentReadFunctions;
import documents.functions.DocumentReadFunctionsFactory;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

  private final Socket client;
  private final Socket controller;
  private ObjectInputStream objectInputStreamClient;
  private ObjectOutputStream objectOutputStreamController;
  private DocumentReadFunctionsFactory documentReadFunctionsFactory;

  private boolean isLoggedIn;
  private User user;

  private ObjectOutputStream objectOutputStreamClient;
  private ObjectInputStream objectInputStreamController;

  public ClientHandler(
      Socket client,
      Socket controller,
      ObjectOutputStream objectOutputStream,
      ObjectInputStream objectInputStream) {
    this.client = client;
    this.controller = controller;
    this.isLoggedIn = false;

    try {
      objectOutputStreamClient = new ObjectOutputStream(client.getOutputStream());
      objectInputStreamClient = new ObjectInputStream(client.getInputStream());
      this.documentReadFunctionsFactory = new DocumentReadFunctionsFactory(objectInputStreamClient);

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
        if (command.equals("createDatabase")
            || command.equals("createCollection")
            || command.equals("addDocument")
            || command.equals("deleteDatabase")
            || command.equals("deleteCollection")
            || command.equals("deleteDocument")
            || command.equals("updateDocument")
            || command.equals("createIndex")) {
          forwardFunctionToController(command);
          String response = getResponseFromController();
          sendMessageToClient(new Packet(response));
          continue;
        }
        DocumentReadFunctions functionToExecute =
            documentReadFunctionsFactory.createDocumentFunction(command);
        if (functionToExecute != null) {
          Object resultFromRead = functionToExecute.execute();
          objectOutputStreamClient.writeObject(resultFromRead);
        }

      } catch (IOException e) {
        try {
          client.close();
        } catch (IOException ex) {
          ex.printStackTrace();
          return;
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
      e.printStackTrace();
      System.out.println("Error sending load to server");
    }
  }

  private void forwardFunctionToController(String command) {
    sendMessageToController(new Packet(command));
    String dbName;
    String collectionName;
    String docId;
    JSONObject object;
    JSONObject schema;
    switch (command) {
      case "createDatabase":
        dbName = getMessageFromClient();
        sendMessageToController(new Packet(dbName));
        return;
      case "createCollection":
        dbName = getMessageFromClient();
        collectionName = getMessageFromClient();
        schema = getJsonFromClient();
        sendMessageToController(new Packet(dbName));
        sendMessageToController(new Packet((collectionName)));
        sendObjectToController(schema);
        return;
      case "addDocument":
        dbName = getMessageFromClient();
        collectionName = getMessageFromClient();
        object = getJsonFromClient();
        sendMessageToController(new Packet(dbName));
        sendMessageToController(new Packet((collectionName)));
        sendObjectToController(object);
        return;
      case "deleteDatabase":
        dbName = getMessageFromClient();
        sendMessageToController(new Packet(dbName));
        return;
      case "deleteCollection":
        dbName = getMessageFromClient();
        collectionName = getMessageFromClient();
        sendMessageToController(new Packet(dbName));
        sendMessageToController(new Packet(collectionName));
        return;
      case "deleteDocument":
        dbName = getMessageFromClient();
        collectionName = getMessageFromClient();
        docId = getMessageFromClient();
        sendMessageToController(new Packet(dbName));
        sendMessageToController(new Packet(collectionName));
        sendMessageToController(new Packet(docId));
      case "updateDocument":
        dbName = getMessageFromClient();
        collectionName = getMessageFromClient();
        docId = getMessageFromClient();
        object = getJsonFromClient();
        sendMessageToController(new Packet(dbName));
        sendMessageToController(new Packet(collectionName));
        sendMessageToController(new Packet(docId));
        sendObjectToController(object);
      case "createIndex":
        dbName = getMessageFromClient();
        collectionName = getMessageFromClient();
        String propertyName = getMessageFromClient();
        sendMessageToController(new Packet(dbName));
        sendMessageToController(new Packet(collectionName));
        sendMessageToController(new Packet(propertyName));
    }
  }

  private void sendMessageToClient(Packet packet) {
    try {
      objectOutputStreamClient.writeObject(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getMessageFromClient() {
    try {
      return ((Packet) objectInputStreamClient.readObject()).getMessage();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  private JSONObject getJsonFromClient() {
    try {
      return (JSONObject) objectInputStreamClient.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  private void sendMessageToController(Packet packet) {
    try {
      objectOutputStreamController.writeObject(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendObjectToController(Object object) {
    try {
      objectOutputStreamController.writeObject(object);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getResponseFromController() {
    try {
      return ((Packet) objectInputStreamController.readObject()).getMessage();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
