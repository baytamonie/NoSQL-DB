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


    } catch (IOException | ClassNotFoundException e) {
      System.out.println("try again, bad login");
    }
  }

  public String getUserAuthority() {
    return userAuthority;
  }

  @Override
  public JSONArray getCollection(String databaseName, String collectionName) {
    if (chainOfResponsibility()
            && userAuthority.contains("r")
            && databaseName != null
            && collectionName != null) {
      String msg = "getCollection";
      try {
        objectOutputStream.writeObject(new Packet(msg));
        objectOutputStream.writeObject(new Packet(databaseName));
        objectOutputStream.writeObject(new Packet(collectionName));

        JSONArray collection =  (JSONArray) objectInputStream.readObject();
        if(collection==null){
          System.out.println("collection is null");
          throw new IllegalArgumentException();
        }
        System.out.println("collection received");
        return collection;
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }

    return null;
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
        JSONObject doc =  (JSONObject) objectInputStream.readObject();

        if(doc==null){
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
            && propertyName !=null) {
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
      String databaseName, String collectionName, String property,String value) {
    if (chainOfResponsibility()
            && userAuthority.contains("r")
            && databaseName != null
            && collectionName != null
            && property != null
            && value !=null) {
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
        for(int i =0;i<count;i++){
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
    if(name==null)
      throw new IllegalArgumentException();
    try {
      objectOutputStream.writeObject(new Packet("createDatabase"));
      objectOutputStream.writeObject(new Packet(name));
      Packet confirmation = (Packet)objectInputStream.readObject();
      System.out.println(confirmation.getMessage());
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void createCollection(String databaseName, String collectionName,JSONObject schema) {
    if(databaseName==null ||collectionName == null)
      throw new IllegalArgumentException();
    try {
      objectOutputStream.writeObject(new Packet("createCollection"));
      objectOutputStream.writeObject(new Packet(databaseName));
      objectOutputStream.writeObject(new Packet(collectionName));
      objectOutputStream.writeObject(schema);
      Packet confirmation = (Packet)objectInputStream.readObject();
      System.out.println(confirmation.getMessage());
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteDatabase(String databaseName) {}

  @Override
  public void deleteCollection(String databaseName, String collectionName) {}

  @Override
  public void writeDocument(String databaseName, String collectionName, JSONObject document) {

    if(databaseName==null ||collectionName == null||document == null)
      throw new IllegalArgumentException();
    try {
      objectOutputStream.writeObject(new Packet("addDocument"));
      objectOutputStream.writeObject(new Packet(databaseName));
      objectOutputStream.writeObject(new Packet(collectionName));
      objectOutputStream.writeObject(document);
      Packet confirmation = (Packet)objectInputStream.readObject();
      System.out.println(confirmation.getMessage());
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void updateDocument(
      String databaseName,
      String collectionName,
      String documentId,
      String propertyName,
      JSONObject newValue) {}

  @Override
  public void deleteDocument(String databaseName, String collectionName, String documentId) {}

  @Override
  public void createIndex(String databaseName, String collectionName, String indexName) {}
}
