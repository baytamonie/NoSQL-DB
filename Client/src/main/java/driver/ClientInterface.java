package driver;

import driver.documentEntities.DocumentEntity;

import java.util.List;

public interface ClientInterface {

    void createConnection();
    void login(String username, String password);
    JSONCollection getCollection(String databaseName,String collectionName);
    JSONDocument getDocument(String databaseName,String collectionName,String documentId);
    DocumentEntity getProperty(String databaseName,String collectionName, String documentId, String propertyName);
    List<DocumentEntity> getProperties(String databaseName,String collectionName, String property);

    void createDatabase(String name);
    void createCollection(String databaseName, String collectionName);
    void deleteDatabase(String databaseName);
    void deleteCollection(String databaseName, String collectionName);
    void writeDocument(String databaseName,String collectionName,JSONDocument document);
    void updateDocument(String databaseName,String collectionName, String documentId,String propertyName, DocumentEntity newValue);
    void deleteDocument(String databaseName,String collectionName, String documentId);

    void createIndex(String databaseName, String collectionName, String indexName);


}
