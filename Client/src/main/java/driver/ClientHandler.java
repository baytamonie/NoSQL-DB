package driver;

import documents.entities.Packet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements ClientInterface {

  private ClientConnector connector;
  private ObjectOutputStream objectOutputStream;
  private ObjectInputStream objectInputStream;
  private Socket socket;
  private boolean isLoggedIn;
  private String userAuthority;

  public static boolean connectionEstablished;

  private boolean chainOfResponsibility() {
    return connectionEstablished;
  }

  public ClientHandler() {
    this.connectionEstablished = false;
    this.isLoggedIn = false;
  }

  public static boolean isConnectionEstablished() {
    return connectionEstablished;
  }

  @Override
  public void createConnection() {
    this.connector = new ClientConnector();
    socket = connector.initConnection();
    if (socket != null) connectionEstablished = true;
    try {
      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      objectInputStream = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void login(String username, String password) {
    if (username == null || password == null) throw new IllegalArgumentException();

    sendMessageToNode(new Packet("login"));
    sendMessageToNode(new Packet(username));
    sendMessageToNode(new Packet(password));
    String loginResponse = getMessageFromNode();
    String[] response = loginResponse.split("\\n");
    String wasLoginSuccessful = response[0];
    if (wasLoginSuccessful.equals("true")) {
      isLoggedIn = true;
      userAuthority = response[1];
      if (userAuthority == null) {
        isLoggedIn = false;
        throw new IllegalArgumentException();
      }
    }
    if (isLoggedIn)
      System.out.println(
          "Success logging in!, you have the following authorities -->  " + userAuthority);
    else System.out.println("Not logged in, try again");
  }

  public String getUserAuthority() {
    return userAuthority;
  }

  @Override
  public JSONArray getCollection(String databaseName, String collectionName) {

    String msg = "getCollection";
    try {
      objectOutputStream.writeObject(new Packet(msg));
      objectOutputStream.writeObject(new Packet(databaseName));
      objectOutputStream.writeObject(new Packet(collectionName));

      JSONArray collection = (JSONArray) objectInputStream.readObject();
      if (collection == null) {
        System.out.println("collection is null");
        throw new IllegalArgumentException();
      }
      System.out.println("collection received");
      return collection;
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public JSONObject getDocument(String databaseName, String collectionName, String documentId) {

    if (chainOfResponsibility()
        && userAuthority.contains("r")
        && databaseName != null
        && collectionName != null
        && documentId != null) {
      String msg = "getDocumentById";
      try {
        objectOutputStream.writeObject(new Packet(msg));
        objectOutputStream.writeObject(new Packet(databaseName));
        objectOutputStream.writeObject(new Packet(collectionName));
        objectOutputStream.writeObject(new Packet(documentId));
        JSONObject doc = (JSONObject) objectInputStream.readObject();

        if (doc == null) {
          System.out.println("document is null");
          throw new IllegalArgumentException();
        }
        System.out.println("Document received");
        return doc;
      } catch (IOException e) {
        System.out.println("Error getting document, IO exception");
        throw new RuntimeException();
      } catch (ClassNotFoundException e) {
        System.out.println("Error getting document, class not found");
        throw new RuntimeException();
      }
    }
    System.out.println("Error getting document");
    throw new RuntimeException();
  }

  @Override
  public Object getProperty(
      String databaseName, String collectionName, String documentId, String propertyName) {
    if (chainOfResponsibility()
        && userAuthority.contains("r")
        && databaseName != null
        && collectionName != null
        && documentId != null
        && propertyName != null) {
      String msg = "getProperty";
      try {
        objectOutputStream.writeObject(new Packet(msg));
        objectOutputStream.writeObject(new Packet(databaseName));
        objectOutputStream.writeObject(new Packet(collectionName));
        objectOutputStream.writeObject(new Packet(documentId));
        objectOutputStream.writeObject(new Packet(propertyName));
        Object obj = objectInputStream.readObject();
        return obj;
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  @Override
  public JSONArray getProperties(
      String databaseName, String collectionName, String property, String value) {
    if (chainOfResponsibility()
        && userAuthority.contains("r")
        && databaseName != null
        && collectionName != null
        && property != null
        && value != null) {
      String msg = "getAllDocumentsWithProperty";
      try {
        objectOutputStream.writeObject(new Packet(msg));
        objectOutputStream.writeObject(new Packet(databaseName));
        objectOutputStream.writeObject(new Packet(collectionName));
        objectOutputStream.writeObject(new Packet(property));
        objectOutputStream.writeObject(new Packet(value));
        JSONArray jsonArray = new JSONArray();
        Packet packet = (Packet) objectInputStream.readObject();
        int count = Integer.valueOf(packet.getMessage());
        System.out.println(count);
        for (int i = 0; i < count; i++) {
          jsonArray.add(objectInputStream.readObject());
        }
        return jsonArray;
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  @Override
  public void createDatabase(String name) {
    if (name == null) throw new IllegalArgumentException();
    try {
      objectOutputStream.writeObject(new Packet("createDatabase"));
      objectOutputStream.writeObject(new Packet(name));
      Packet confirmation = (Packet) objectInputStream.readObject();
      System.out.println(confirmation.getMessage());
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void createCollection(String databaseName, String collectionName, JSONObject schema) {
    if (databaseName == null || collectionName == null || schema == null)
      throw new IllegalArgumentException();
    try {
      objectOutputStream.writeObject(new Packet("createCollection"));
      objectOutputStream.writeObject(new Packet(databaseName));
      objectOutputStream.writeObject(new Packet(collectionName));
      objectOutputStream.writeObject(schema);
      Packet confirmation = (Packet) objectInputStream.readObject();
      System.out.println(confirmation.getMessage());
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteDatabase(String databaseName) {
    if (databaseName == null) throw new IllegalArgumentException();
    sendMessageToNode(new Packet("deleteDatabase"));
    sendMessageToNode(new Packet(databaseName));
    String confirmation = getMessageFromNode();
    if (confirmation.equals("true"))
      System.out.println("Deleting database " + databaseName + " is successful");
    else System.out.println("Deleting database " + databaseName + " has failed");
  }

  @Override
  public void deleteCollection(String databaseName, String collectionName) {
    if (databaseName == null || collectionName == null) throw new IllegalArgumentException();

    sendMessageToNode(new Packet("deleteCollection"));
    sendMessageToNode(new Packet(databaseName));
    sendMessageToNode(new Packet(collectionName));
    String confirmation = getMessageFromNode();
    if (confirmation.equals("true"))
      System.out.println("Deleting collection " + collectionName + " successful");
    else System.out.println("Deleting collection " + collectionName + " failed");
  }

  @Override
  public void writeDocument(String databaseName, String collectionName, JSONObject document) {

    if (databaseName == null || collectionName == null || document == null)
      throw new IllegalArgumentException();
    sendMessageToNode(new Packet("addDocument"));
    sendMessageToNode(new Packet(databaseName));
    sendMessageToNode(new Packet(collectionName));
    sendObjectToNode(document);
    String confirmation = getMessageFromNode();
    if (confirmation.equals("true"))
      System.out.println("Writing document " + document.toJSONString() + " is successful");
    else System.out.println("Writing document " + document.toJSONString() + " has failed");
  }

  @Override
  public void updateDocument(
      String databaseName,
      String collectionName,
      String documentId,
      JSONObject newValue) {
    if (databaseName == null
        || collectionName == null
        || documentId == null
        || newValue == null) throw new IllegalArgumentException();
    sendMessageToNode(new Packet("updateDocument"));
    sendMessageToNode(new Packet(databaseName));
    sendMessageToNode(new Packet(collectionName));
    sendMessageToNode(new Packet(documentId));
    sendObjectToNode(newValue);
    String confirmation = getMessageFromNode();
    if (confirmation.equals("true"))
      System.out.println("Updating document " + documentId + " is successful");
    else System.out.println("Updating document " + documentId + " has failed");

  }

  @Override
  public void deleteDocument(String databaseName, String collectionName, String documentId) {
    if (databaseName == null || collectionName == null || documentId == null)
      throw new IllegalArgumentException();
    sendMessageToNode(new Packet("deleteDocument"));
    sendMessageToNode(new Packet(databaseName));
    sendMessageToNode(new Packet(collectionName));
    sendMessageToNode(new Packet(documentId));
    String confirmation = getMessageFromNode();
    if (confirmation.equals("true"))
      System.out.println("Deleting document " + documentId + " is successful");
    else System.out.println("Deleting document " + documentId + " has failed");
  }

  @Override
  public void createIndex(String databaseName, String collectionName, String indexName) {
    if (databaseName == null || collectionName == null || indexName == null)
      throw new IllegalArgumentException();
  }

  private void sendMessageToNode(Packet packet) {
    if (packet == null) throw new IllegalArgumentException();
    try {
      objectOutputStream.writeObject(packet);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  private void sendObjectToNode(Object object) {
    try {
      objectOutputStream.writeObject(object);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  private String getMessageFromNode() {
    try {
      Packet packet = (Packet) objectInputStream.readObject();
      return packet.getMessage();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
