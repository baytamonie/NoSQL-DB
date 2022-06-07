package driver;

import driver.documentEntities.DocumentEntity;

import java.util.List;

public class ClientHandler implements ClientInterface {

    @Override
    public void createConnection() {

    }

    @Override
    public JSONCollection getCollection(String databaseName, String collectionName) {
        return null;
    }

    @Override
    public JSONDocument getDocument(String databaseName, String collectionName, String documentId) {
        return null;
    }

    @Override
    public DocumentEntity getProperty(String databaseName, String collectionName, String documentId, String propertyName) {
        return null;
    }

    @Override
    public List<DocumentEntity> getProperties(String databaseName, String collectionName, String property) {
        return null;
    }

    @Override
    public void createDatabase(String name) {

    }

    @Override
    public void createCollection(String databaseName, String collectionName) {

    }

    @Override
    public void deleteDatabase(String databaseName) {

    }

    @Override
    public void deleteCollection(String databaseName, String collectionName) {

    }

    @Override
    public void writeDocument(String databaseName, String collectionName, JSONDocument document) {

    }

    @Override
    public void updateDocument(String databaseName, String collectionName, String documentId, String propertyName, DocumentEntity newValue) {

    }

    @Override
    public void deleteDocument(String databaseName, String collectionName, String documentId) {

    }

    @Override
    public void createIndex(String databaseName, String collectionName, String indexName) {

    }
}
