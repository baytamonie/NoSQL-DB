package documents.functions;

import org.json.simple.JSONObject;

public class UpdateDocument implements DatabaseWriteFunction{

    private final String dbName;
    private final String collectionName;
    private final String docId;
    private final JSONObject newObject;

    public  UpdateDocument(String dbName, String collectionName, String docId, JSONObject newObject) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.docId = docId;
        this.newObject = newObject;
    }

    @Override
    public synchronized boolean execute() {
        String path = "src/main/resources/databases/" + dbName +'/' +collectionName;
        if(!fileUtils.checkIfFileOrDirectoryExists(path)){
            return false;
        }
        DeleteDocument delete = new DeleteDocument(dbName,collectionName,docId);
        if(!delete.execute())
            return false;
        AddDocumentToCollection addDocumentToCollection = new AddDocumentToCollection(dbName,collectionName,newObject);
        return addDocumentToCollection.execute();
    }
}
