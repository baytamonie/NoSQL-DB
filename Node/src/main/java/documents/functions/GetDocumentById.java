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
    public Object execute(  ) {

            if(!DocumentUtils.checkIfCollectionExists(dbName,collectionName))
                return null;

            String idsDocPath = DocumentUtils.pathBuilder(dbName,collectionName,"ids.json");
            HashMap<String, IdsObject> ids = FileUtils.loadIdsJSON(idsDocPath);
            IdsObject idsObject = ids.get(documentId);
            if(idsObject == null)
                return null;
            String dataPath = DocumentUtils.pathBuilder(dbName,collectionName,"data.json");
            JSONObject jsonObject =
                    FileUtils.getObjectRandomAccessFile(dataPath, idsObject.getBegin(), idsObject.getEnd());
            return jsonObject;

    }
}
