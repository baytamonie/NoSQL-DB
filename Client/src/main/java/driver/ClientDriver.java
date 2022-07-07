package driver;

import documents.entities.Packet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientDriver implements ClientInterface {

  private ObjectOutputStream nodeObjectOutputStream,controllerObjectOutputStream;
  private ObjectInputStream nodeObjectInputStream,controllerObjectInputStream;
  private boolean isLoggedIn;
  private String userAuthority;

  public static boolean connectionEstablished;

  public ClientDriver() {
    connectionEstablished = false;
    this.isLoggedIn = false;
    userAuthority = "";

  }

  public static boolean isConnectionEstablished() {
    return connectionEstablished;
  }

  @Override
  public void createConnection() {

    try {
      ClientConnector connector = new ClientConnector();
      Socket socket = connector.initConnection();
      nodeObjectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      nodeObjectInputStream = new ObjectInputStream(socket.getInputStream());
      controllerObjectInputStream = connector.getControllerInputStream();
      controllerObjectOutputStream = connector.getControllerOutputStream();
      connectionEstablished = true;
    } catch (Exception e) {
      connectionEstablished = false;
    }
  }

  @Override
  public void login(String username, String password) {
    if (username == null || password == null) throw new IllegalArgumentException();
    if (isConnectionEstablished()) {
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
      return;
    }
    System.out.println("please make sure to establish a connection before logging in..");
  }

  private boolean canUserPerformAction(char action) {
    return userAuthority.indexOf(action) != -1;
  }

  @Override
  public JSONArray getCollection(String databaseName, String collectionName) {
    if (databaseName == null || collectionName == null) throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('r')) {
      String msg = "getCollection";
      sendMessageToNode(new Packet(msg));
      sendMessageToNode(new Packet(databaseName));
      sendMessageToNode(new Packet(collectionName));
      JSONArray collection = (JSONArray) getObjectFromNode();

      if (collection == null) {
        System.out.println("collection is null");
        throw new IllegalArgumentException();
      }
      System.out.println("success, collection received");
      return collection;
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to read");
  }

  @Override
  public JSONObject getDocument(String databaseName, String collectionName, String documentId) {
    if (databaseName == null || collectionName == null || documentId == null)
      throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('r')) {
      String msg = "getDocumentById";
      sendMessageToNode(new Packet(msg));
      sendMessageToNode(new Packet(databaseName));
      sendMessageToNode(new Packet(collectionName));
      sendMessageToNode(new Packet(documentId));
      JSONObject doc = (JSONObject) getObjectFromNode();
      if (doc == null) {
        System.out.println("document is null");
        throw new IllegalArgumentException();
      }
      System.out.println("success, document received");
      return doc;
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to read");
  }

  @Override
  public Object getProperty(
      String databaseName, String collectionName, String documentId, String propertyName) {
    if (databaseName == null
        || collectionName == null
        || documentId == null
        || propertyName == null) throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('r')) {
      String msg = "getProperty";
      sendMessageToNode(new Packet(msg));
      sendMessageToNode(new Packet(databaseName));
      sendMessageToNode(new Packet(collectionName));
      sendMessageToNode(new Packet(documentId));
      sendMessageToNode(new Packet(propertyName));
      Object obj = getObjectFromNode();
      if (obj == null) {
        System.out.println("Object is null");
        throw new IllegalArgumentException();
      }
      System.out.println("success, document received");
      return obj;
    }

    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to read");
  }

  @Override
  public JSONArray getProperties(
      String databaseName, String collectionName, String property, String value) {
    if (databaseName == null || collectionName == null || property == null || value == null)
      throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('r')) {
      sendMessageToNode(new Packet("getAllDocumentsWithProperty"));
      sendMessageToNode(new Packet(databaseName));
      sendMessageToNode(new Packet(collectionName));
      sendMessageToNode(new Packet(property));
      sendMessageToNode(new Packet(value));
      try {
        JSONArray jsonArray = (JSONArray) getObjectFromNode();
        if (jsonArray == null)
          System.out.println(
              "No values were found in the collection with the property value " + value);
        else System.out.println("success, collection received");
        return jsonArray;
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException();
      }
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to read");
  }

  @Override
  public void createDatabase(String dbName) {
    if (dbName == null) throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('w')) {
      sendMsgToController(new Packet("createDatabase"));
      sendMsgToController(new Packet(dbName));
      String confirmation = getMsgFromController();
      if (confirmation!=null &&confirmation.equals("true")){
        System.out.println("Creating database " + dbName + " is successful");

      }
      else System.out.println("Creating database " + dbName + " has failed");
      getMsgFromController();
      return;
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to write");
  }

  @Override
  public void createCollection(String databaseName, String collectionName, JSONObject schema) {
    if (databaseName == null || collectionName == null || schema == null)
      throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('w')) {
      sendMsgToController(new Packet("createCollection"));
      sendMsgToController(new Packet(databaseName));
      sendMsgToController(new Packet(collectionName));
      sendObjectToController(schema);
      String confirmation = getMsgFromController();
      if (confirmation!=null && confirmation.equals("true")){
        System.out.println("Creating collection " + collectionName + " is successful");

      }
      else System.out.println("Creating collection " + collectionName + " has failed");
      getMsgFromController();
      return;
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to write");
  }

  @Override
  public void deleteDatabase(String databaseName) {
    if (databaseName == null) throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('w')) {
      sendMsgToController(new Packet("deleteDatabase"));
      sendMsgToController(new Packet(databaseName));
      String confirmation = getMsgFromController();
      if (confirmation!=null &&confirmation.equals("true")){
        System.out.println("Deleting database " + databaseName + " is successful");
      }
      else System.out.println("Deleting database " + databaseName + " has failed");
      getMsgFromController();
      return;
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to write");
  }

  @Override
  public void deleteCollection(String databaseName, String collectionName) {
    if (databaseName == null || collectionName == null) throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('w')) {
      sendMsgToController(new Packet("deleteCollection"));
      sendMsgToController(new Packet(databaseName));
      sendMsgToController(new Packet(collectionName));
      String confirmation = getMessageFromNode();
      if (confirmation.equals("true")){
        System.out.println("Deleting collection " + collectionName + " successful");
      }
      else System.out.println("Deleting collection " + collectionName + " failed");
      getMsgFromController();
      return;
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to write");
  }

  @Override
  public void writeDocument(String databaseName, String collectionName, JSONObject document) {

    if (databaseName == null || collectionName == null || document == null)
      throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('w')) {
      sendMsgToController(new Packet("addDocument"));
      sendMsgToController(new Packet(databaseName));
      sendMsgToController(new Packet(collectionName));
      sendObjectToController(document);
      String confirmation = getMsgFromController();
      System.out.println(confirmation);
      if (confirmation!=null &&confirmation.equals("true")){
        System.out.println("Writing document " + document.toJSONString() + " is successful");
      }
      else System.out.println("Writing document " + document.toJSONString() + " has failed");
      getMsgFromController();
      return;
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to write");
  }

  @Override
  public void updateDocument(
      String databaseName, String collectionName, String documentId, JSONObject newValue) {
    if (databaseName == null || collectionName == null || documentId == null || newValue == null)
      throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('w')) {
      sendMsgToController(new Packet("updateDocument"));
      sendMsgToController(new Packet(databaseName));
      sendMsgToController(new Packet(collectionName));
      sendMsgToController(new Packet(documentId));
      sendObjectToNode(newValue);
      String confirmation = getMsgFromController();
      if (confirmation!=null &&confirmation.equals("true")){
        System.out.println("Updating document " + documentId + " is successful");

      }
      else System.out.println("Updating document " + documentId + " has failed");
      getMsgFromController();
      return;
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to write");
  }

  @Override
  public void deleteDocument(String databaseName, String collectionName, String documentId) {
    if (databaseName == null || collectionName == null || documentId == null)
      throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('w')) {
      sendMsgToController(new Packet("deleteDocument"));
      sendMsgToController(new Packet(databaseName));
      sendMsgToController(new Packet(collectionName));
      sendMsgToController(new Packet(documentId));
      String confirmation = getMsgFromController();
      if (confirmation!=null &&confirmation.equals("true")){
        System.out.println("Deleting document " + documentId + " is successful");

      }
      else System.out.println("Deleting document " + documentId + " has failed");
      getMsgFromController();
      return;
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to write");
  }

  @Override
  public void createIndex(String databaseName, String collectionName, String indexName) {

    if (databaseName == null || collectionName == null || indexName == null)
      throw new IllegalArgumentException();
    if (connectionEstablished && isLoggedIn && canUserPerformAction('i')) {
      sendMsgToController(new Packet("createIndex"));
      sendMsgToController(new Packet(databaseName));
      sendMsgToController(new Packet(collectionName));
      sendMsgToController(new Packet(indexName));
      String confirmation = getMsgFromController();
      if (confirmation!=null &&confirmation.equals("true")){
        System.out.println("creating index on " + indexName + " is successful");

      }
      else System.out.println("creating index on " + indexName + " has failed");
      getMsgFromController();
      return;
    }
    throw new RuntimeException(
        "Make sure to check connection, log in and have authorities to create index");
  }

  private void sendMessageToNode(Packet packet) {
    if (packet == null) throw new IllegalArgumentException();
    try {
      nodeObjectOutputStream.writeObject(packet);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  private void sendObjectToNode(Object object) {
    try {
      nodeObjectOutputStream.writeObject(object);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  private String getMessageFromNode() {
    try {
      Packet packet = (Packet) nodeObjectInputStream.readObject();
      return packet.getMessage();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  private Object getObjectFromNode() {
    try {
      return nodeObjectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  private void sendMsgToController(Packet msg){
    try{
      controllerObjectOutputStream.writeObject(msg);
    }catch (Exception e){
      e.printStackTrace();
    }
  }private void sendObjectToController(Object obj){
    try{
      controllerObjectOutputStream.writeObject(obj);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  private String getMsgFromController(){
    try{
      return ((Packet)controllerObjectInputStream.readObject()).getMessage();
    }catch (Exception e){

      e.printStackTrace();
      return null;
    }
  }
  private Object getObjectFromController(){
    try{
      return controllerObjectInputStream.readObject();
    }catch (Exception e){
      e.printStackTrace();
      return null;
    }
  }


}
