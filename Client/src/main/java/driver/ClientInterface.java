package driver;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public interface ClientInterface {

    void createConnection();
    void login(String username, String password);
    JSONArray getCollection(String databaseName, String collectionName);
    JSONObject getDocument(String databaseName, String collectionName, String documentId);
    Object getProperty(String databaseName,String collectionName, String documentId, String propertyName);
    List<JSONObject> getProperties(String databaseName,String collectionName, String property,String value);

    void createDatabase(String name);
    void createCollection(String databaseName, String collectionName,JSONObject schema);
    void deleteDatabase(String databaseName);
    void deleteCollection(String databaseName, String collectionName);
    void writeDocument(String databaseName,String collectionName,JSONObject document);
    void updateDocument(String databaseName,String collectionName, String documentId,String propertyName,JSONObject newValue);
    void deleteDocument(String databaseName,String collectionName, String documentId);

    void createIndex(String databaseName, String collectionName, String indexName);


}
