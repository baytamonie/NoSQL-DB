import com.google.gson.JsonParser;
import documents.IdsObject;
import documents.entities.Packet;
import documents.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utilities.DocumentUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ClientHandler implements Runnable {

  private final Socket client;
  private final Socket controller;
  private DataInputStream dataInputStreamClient;
  private ObjectInputStream objectInputStreamClient;
  private ObjectOutputStream objectOutputStreamController;

  private boolean isLoggedIn;
  private User user;

  private final ObjectOutputStream objectOutputStreamClient;

  public ClientHandler(Socket client, Socket controller, ObjectOutputStream objectOutputStream) {
    this.client = client;
    this.controller = controller;
    this.isLoggedIn = false;
    try {
      objectOutputStreamClient = new ObjectOutputStream(client.getOutputStream());
      objectInputStreamClient = new ObjectInputStream(client.getInputStream());
      objectOutputStreamController = objectOutputStream;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void getCollection() {
    try {
      Packet dbName = (Packet) objectInputStreamClient.readObject();
      Packet collectionName = (Packet) objectInputStreamClient.readObject();
      if (DocumentUtils.checkIfCollectionExists(dbName.getMessage(), collectionName.getMessage())) {
        String path =
            DocumentUtils.pathBuilder(
                dbName.getMessage(), collectionName.getMessage(), "data.json");
        JSONArray jsonArray = FileHandler.loadData(path);
        objectOutputStreamClient.writeObject(jsonArray);
      } else objectOutputStreamClient.writeObject(null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
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

  public void getDocumentById() {
    try {
      Packet dbName = (Packet) objectInputStreamClient.readObject();
      Packet collectionName = (Packet) objectInputStreamClient.readObject();
      Packet id = (Packet) objectInputStreamClient.readObject();
      if (DocumentUtils.checkIfCollectionExists(dbName.getMessage(), collectionName.getMessage())) {
        String path =
            DocumentUtils.pathBuilder(dbName.getMessage(), collectionName.getMessage(), "ids.json");
        HashMap<String, IdsObject> ids = FileHandler.loadIdsJSON(path);
        path =
            DocumentUtils.pathBuilder(
                dbName.getMessage(), collectionName.getMessage(), "data.json");
        IdsObject idsObject = ids.get(id.getMessage());
        JSONObject jsonObject =
            FileHandler.getObjectRandomAccessFile(path, idsObject.getBegin(), idsObject.getEnd());

        objectOutputStreamClient.writeObject((Object) jsonObject);

      } else objectOutputStreamClient.writeObject(null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  public void getProperty(){
    try {
      Packet dbName = (Packet) objectInputStreamClient.readObject();
      Packet collectionName = (Packet) objectInputStreamClient.readObject();
      Packet id = (Packet) objectInputStreamClient.readObject();
      Packet property = (Packet) objectInputStreamClient.readObject();

      if (DocumentUtils.checkIfCollectionExists(dbName.getMessage(), collectionName.getMessage())) {
        String path =
                DocumentUtils.pathBuilder(dbName.getMessage(), collectionName.getMessage(), "ids.json");
        HashMap<String, IdsObject> ids = FileHandler.loadIdsJSON(path);
        path =
                DocumentUtils.pathBuilder(
                        dbName.getMessage(), collectionName.getMessage(), "data.json");
        IdsObject idsObject = ids.get(id.getMessage());
        JSONObject jsonObject =
                FileHandler.getObjectRandomAccessFile(path, idsObject.getBegin(), idsObject.getEnd());
        Object obj = jsonObject.get(property.getMessage());
        objectOutputStreamClient.writeObject(obj);
      } else objectOutputStreamClient.writeObject(null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  public void getAllDocumentsWithProperty(){
    try {
      Packet dbName = (Packet) objectInputStreamClient.readObject();
      Packet collectionName = (Packet) objectInputStreamClient.readObject();
      Packet property = (Packet) objectInputStreamClient.readObject();
      Packet propertyValue = (Packet) objectInputStreamClient.readObject();
      if (DocumentUtils.checkIfCollectionExists(dbName.getMessage(), collectionName.getMessage())) {
       String path =
                DocumentUtils.pathBuilder(
                        dbName.getMessage(), collectionName.getMessage(), "data.json");
        JSONArray jsonArray = FileHandler.loadData(path);
        int count = 0;
        for(int i =0;i<jsonArray.size();i++){
          if (((JSONObject) jsonArray.get(i))
              .get(property.getMessage())
              .equals(propertyValue.getMessage())) System.out.println("HEHEHE");
          System.out.println("EAEA");
        }
        System.out.println("ASDASD");
        System.out.println(count);
        objectOutputStreamClient.writeObject(new Packet(String.valueOf(count)));
        System.out.println(count);

      }
      else objectOutputStreamClient.writeObject(null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    while (!client.isClosed()) {

      Packet command;
      try {
        command = (Packet) objectInputStreamClient.readObject();
        System.out.println(command);
        switch (command.getMessage()) {
          case "login":
            login();
            break;
          case "getDocumentById":
            getDocumentById();
            break;
          case "getCollection":
            getCollection();
            break;
          case "getProperty":
            getProperty();
            break;
          case "getAllDocumentsWithProperty":
            getAllDocumentsWithProperty();
            break;
        }
      } catch (Exception e) {
        System.out.println(e.getCause());
        break;
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
