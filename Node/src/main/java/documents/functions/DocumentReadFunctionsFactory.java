package documents.functions;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import documents.entities.Packet;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;

public class DocumentReadFunctionsFactory {

    private final ObjectInputStream objectInputStream;

    public DocumentReadFunctionsFactory(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
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
                dbName = getPacket();
                collectionName = getPacket();
                documentId = getPacket();
                return new GetDocumentById(dbName,collectionName,documentId);
            case "getProperty":
                dbName = getPacket();
                collectionName = getPacket();
                documentId = getPacket();
                propertyName = getPacket();
                return new GetPropertyValueFromDocument(dbName,collectionName,documentId,propertyName);
            case "login":
                userName = getPacket();
                password = getPacket();
                return new Login(userName,password);
            case "getAllDocumentsWithProperty":
                return new GetAllDocumentsWithProperty();
            case "getCollection":
                dbName = getPacket();
                collectionName = getPacket();
                return new GetCollectionFromDatabase(dbName,collectionName);
            default:
                throw new IllegalArgumentException();
        }
    }

    public String getPacket(){
        try {
            return ((Packet)objectInputStream.readObject()).getMessage();
        } catch (IOException  |ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getJsonObject(){
        try {
            return ((JSONObject)objectInputStream.readObject());
        } catch (IOException  |ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
