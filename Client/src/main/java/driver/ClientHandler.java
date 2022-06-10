package driver;

import documents.entities.Packet;
import driver.documentEntities.DocumentEntity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements ClientInterface {

  private ClientConnector connector;
  private ObjectOutputStream objectOutputStream;
  private ObjectInputStream objectInputStream;
  private Socket socket;
  private boolean isLoggedIn;
  private String userAuthority;

  public static boolean connectionEstablished;

  private boolean chainOfResponsibility() {
    if (connectionEstablished) return true;
    else return false;
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
    if (socket != null)
      connectionEstablished = true;
  }

  @Override
  public void login(String username, String password) {
    if(connectionEstablished)
    try {
      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      objectOutputStream.writeObject(new Packet("login"));
      objectOutputStream.writeObject(new Packet(username));
      objectOutputStream.writeObject(new Packet(password));
      objectInputStream = new ObjectInputStream(socket.getInputStream());
      Packet loginResponse = (Packet)objectInputStream.readObject();
      if(loginResponse.getMessage().equals("true")){
        isLoggedIn = true;
        Packet authority = (Packet)objectInputStream.readObject();
        userAuthority = authority.getMessage();
        System.out.println("logged in successfully");
      }
      else{
        isLoggedIn = false;
      }


    } catch (IOException e) {
      System.out.println("try again, bad login");
    } catch (ClassNotFoundException e) {
      System.out.println("try again, bad login");
    }
  }

  @Override
  public JSONCollection getCollection(String databaseName, String collectionName) {
    return null;
  }

  @Override
  public JSONDocument getDocument(String databaseName, String collectionName, String documentId) {
    if (chainOfResponsibility()
        && databaseName != null
        && collectionName != null
        && documentId != null) {
      String msg = "getDocument" + "\n" + databaseName + "\n" + collectionName + "\n" + documentId;
      try {
        objectOutputStream.writeObject(new Packet(msg));
        JSONDocument doc = (JSONDocument) objectInputStream.readObject();
        return doc;
      } catch (IOException e) {
        System.out.println("Error getting document");
        return null;
      } catch (ClassNotFoundException e) {
        System.out.println("Error getting document");
        return null;
      }
    }
    return null;
  }

  @Override
  public DocumentEntity getProperty(
      String databaseName, String collectionName, String documentId, String propertyName) {
    return null;
  }

  @Override
  public List<DocumentEntity> getProperties(
      String databaseName, String collectionName, String property) {
    return null;
  }

  @Override
  public void createDatabase(String name) {}

  @Override
  public void createCollection(String databaseName, String collectionName) {}

  @Override
  public void deleteDatabase(String databaseName) {}

  @Override
  public void deleteCollection(String databaseName, String collectionName) {}

  @Override
  public void writeDocument(String databaseName, String collectionName, JSONDocument document) {}

  @Override
  public void updateDocument(
      String databaseName,
      String collectionName,
      String documentId,
      String propertyName,
      DocumentEntity newValue) {}

  @Override
  public void deleteDocument(String databaseName, String collectionName, String documentId) {}

  @Override
  public void createIndex(String databaseName, String collectionName, String indexName) {}
}
