package documents.functions;

import documents.entities.Packet;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;

public class DocumentReadFunctionsFactory {

    private final ObjectInputStream clientObjectInputStream;

    public DocumentReadFunctionsFactory(ObjectInputStream clientObjectInputStream) {
        this.clientObjectInputStream = clientObjectInputStream;
    }

    public DocumentReadFunctions createDocumentFunction(String function){
        String dbName;
        String collectionName;
        String documentId;
        String propertyName;
        String userName;
        String password;
        if( function == null || function.isEmpty())
            throw new IllegalArgumentException();
        switch (function){
            case "getDocumentById":
                dbName = getPacketFromClient();
                collectionName = getPacketFromClient();
                documentId = getPacketFromClient();
                return new GetDocumentById(dbName,collectionName,documentId);
            case "getProperty":
                dbName = getPacketFromClient();
                collectionName = getPacketFromClient();
                documentId = getPacketFromClient();
                propertyName = getPacketFromClient();
                return new GetPropertyValueFromDocument(dbName,collectionName,documentId,propertyName);
            case "login":
                userName = getPacketFromClient();
                password = getPacketFromClient();
                return new Login(userName,password);
            case "getAllDocumentsWithProperty":
                return new GetAllDocumentsWithProperty();
            case "getCollection":
                dbName = getPacketFromClient();
                collectionName = getPacketFromClient();
                return new GetCollectionFromDatabase(dbName,collectionName);
            default:
                return null;
        }
    }

    public String getPacketFromClient(){
        try {
            return ((Packet) clientObjectInputStream.readObject()).getMessage();
        } catch (IOException  |ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getJsonObject(){
        try {
            return ((JSONObject) clientObjectInputStream.readObject());
        } catch (IOException  |ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
