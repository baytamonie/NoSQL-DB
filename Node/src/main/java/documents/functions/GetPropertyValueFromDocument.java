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

public class GetPropertyValueFromDocument implements DocumentReadFunctions {

    private final String dbName;
    private final String collectionName;
    private final String documentId;
    private final String propertyName;


    public GetPropertyValueFromDocument(String dbName, String collectionName, String documentId, String propertyName) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.documentId = documentId;
        this.propertyName = propertyName;
    }

    @Override
    public Object execute( ) {

            if (!DocumentUtils.checkIfCollectionExists(dbName, collectionName))
                return null;
                String idsDocPath =
                        DocumentUtils.pathBuilder(dbName, collectionName, "ids.json");
                HashMap<String, IdsObject> ids = FileUtils.loadIdsJSON(idsDocPath);
                String docPath =
                        DocumentUtils.pathBuilder(
                                dbName, collectionName, "data.json");
                IdsObject idsObject = ids.get(documentId);
                if(idsObject==null)
                    return null;
                JSONObject jsonObject =
                        FileUtils.getObjectRandomAccessFile(docPath, idsObject.getBegin(), idsObject.getEnd());
                if(jsonObject==null)
                    return null;
                Object obj = jsonObject.get(propertyName);
                return obj;

    }
}
