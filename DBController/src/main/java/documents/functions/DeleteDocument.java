package documents.functions;

import documents.entities.IdsObject;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class DeleteDocument implements DatabaseWriteFunction {

    private final String dbName;
    private final String collectionName;
    private final String documentId;


    public DeleteDocument(String dbName, String collectionName, String documentId) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.documentId = documentId;
    }

    @Override
    public synchronized boolean execute() {
        String path = "src/main/resources/databases"+'/'+dbName+'/'+collectionName;
        if(!fileUtils.checkIfFileOrDirectoryExists(path))
            return false;
        documentsUtils.generateKey(dbName,collectionName);
        indexingUtils.deleteObjectFromIndexedFiles(getDeletedObject(dbName,collectionName,documentId),dbName,collectionName);
      return idsUtils.deleteFromIds(path+"/ids.json",documentId);

    }

    private JSONObject getDeletedObject(String dbName, String collectionName, String documentId){
        String idsDocPath = fileUtils.pathBuilder(dbName, collectionName, "ids.json");
        HashMap<String, IdsObject> ids = idsUtils.loadIdsJSON(idsDocPath);
        IdsObject idsObject = ids.get(documentId);
        if (idsObject == null) return null;
        String dataPath = fileUtils.pathBuilder(dbName, collectionName, "data.json");
        JSONObject jsonObject =
                fileUtils.getObjectRandomAccessFile(dataPath, idsObject.getBegin(), idsObject.getEnd());
        return jsonObject;
    }


}
