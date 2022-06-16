package documents.functions;

import documents.entities.Packet;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;

public class DatabaseFunctionsFactory {

  private final ObjectInputStream objectInputStream;
  public DatabaseFunctionsFactory(ObjectInputStream objectInputStream) {
    this.objectInputStream = objectInputStream;
  }

  public DatabaseWriteFunction getDataBaseFunction(String function) {
    String dbName;
    String collectionName;
    JSONObject schema;
    JSONObject document;
    switch (function) {
      case "createDatabase":
         dbName = getPacket();
        return new CreateDataBase(dbName);
      case "createCollection":
         dbName = getPacket();
         collectionName = getPacket();
         schema = getJsonObject();
        return new CreateCollection(dbName,collectionName,schema);
      case "addDocument":
        dbName = getPacket();
        collectionName = getPacket();
        document = getJsonObject();
        return new AddDocumentToCollection(dbName,collectionName,document);
      case "DeleteDataBase":
        dbName = getPacket();
        return new DeleteDataBase(dbName);
      case "DeleteCollection":
        dbName = getPacket();
        collectionName = getPacket();
        return new DeleteCollection(dbName,collectionName);
      default:
        return null;
    }
  }

  private String getPacket(){
    try {
      return ((Packet) objectInputStream.readObject()).getMessage();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  private JSONObject getJsonObject(){
    try {
      return (JSONObject) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

}
