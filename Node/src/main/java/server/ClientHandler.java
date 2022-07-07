package server;

import documents.entities.Packet;
import documents.functions.DocumentReadFunctions;
import documents.functions.DocumentReadFunctionsFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

  private final Socket client;

  private ObjectInputStream objectInputStreamClient;

  private DocumentReadFunctionsFactory documentReadFunctionsFactory;


  private static ObjectOutputStream objectOutputStreamClient;


  public ClientHandler(
      Socket client) {
    this.client = client;

    try {
      objectOutputStreamClient = new ObjectOutputStream(client.getOutputStream());
      objectInputStreamClient = new ObjectInputStream(client.getInputStream());
      this.documentReadFunctionsFactory = new DocumentReadFunctionsFactory(objectInputStreamClient);


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



}
