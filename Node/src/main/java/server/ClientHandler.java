package server;

import databaseTransfer.HorizontalScaling;
import documents.entities.Packet;
import documents.functions.DocumentReadFunctions;
import documents.functions.DocumentReadFunctionsFactory;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ClientHandler implements Runnable {

  private final Socket client;

  private ObjectInputStream objectInputStreamClient;
  private ObjectOutputStream objectOutputStreamController;
  private DocumentReadFunctionsFactory documentReadFunctionsFactory;
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();



  private static ObjectOutputStream objectOutputStreamClient;
  private ObjectInputStream objectInputStreamController;

  public ClientHandler(
      Socket client,
      ObjectOutputStream objectOutputStream,
      ObjectInputStream objectInputStream) {
    this.client = client;

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
        DocumentReadFunctions functionToExecute =
            documentReadFunctionsFactory.createDocumentFunction(command);
        if (functionToExecute != null) {
          Object resultFromRead = functionToExecute.execute();
          objectOutputStreamClient.writeObject(resultFromRead);
        }

      } catch (IOException e) {
        try {
          client.close();
          objectOutputStreamClient.close();
          objectInputStreamClient.close();
        } catch (IOException ex) {
          ex.printStackTrace();
          return;
        }
        e.printStackTrace();

      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }

  }




  public static void sendMessageToClient(Packet packet) {
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

  public String getMessageFromController(){
    try {
      return ((Packet) objectInputStreamController.readObject()).getMessage();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
  private void sendObjectToController(Object object) {
    try {
      objectOutputStreamController.writeObject(object);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
