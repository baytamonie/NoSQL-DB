package documents.functions;


import documents.IdsObject;
import documents.entities.Packet;
import org.json.simple.JSONObject;
import utilities.DocumentUtils;
import utilities.FileUtils;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class GetDocumentById implements DocumentReadFunctions {


    private final String dbName;
    private final String collectionName;
    private final String documentId;

    public GetDocumentById(String dbName, String collectionName, String documentId) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.documentId = documentId;
    System.out.println(dbName);
    System.out.println(collectionName);
    System.out.println(documentId);
    }

    @Override
    public boolean execute( ObjectOutputStream outputStream) {

        try {
            if(!DocumentUtils.checkIfCollectionExists(dbName,collectionName))
                return false;
            if(!DocumentUtils.checkIfCollectionExists(dbName,collectionName))
            return false;

            String idsDocPath = DocumentUtils.pathBuilder(dbName,collectionName,"ids.json");
            HashMap<String, IdsObject> ids = FileUtils.loadIdsJSON(idsDocPath);
            IdsObject idsObject = ids.get(documentId);
            if(idsObject == null)
                return false;
            String dataPath = DocumentUtils.pathBuilder(dbName,collectionName,"data.json");
            JSONObject jsonObject =
                    FileUtils.getObjectRandomAccessFile(dataPath, idsObject.getBegin(), idsObject.getEnd());
            if(jsonObject==null)
                return false;
            outputStream.writeObject(jsonObject);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }
}
