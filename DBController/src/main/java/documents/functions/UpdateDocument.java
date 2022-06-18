package documents.functions;

import org.json.simple.JSONObject;
import utils.DocumentsUtils;
import utils.FileUtils;

public class UpdateDocument implements DatabaseWriteFunction{

    private final String dbName;
    private final String collectionName;
    private final String docId;
    private final JSONObject newObject;

    public UpdateDocument(String dbName, String collectionName, String docId, JSONObject newObject) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.docId = docId;
        this.newObject = newObject;
    }

    @Override
    public boolean execute() {
        String path = "src/main/resources/databases/" + dbName +'/' +collectionName;
        if(!FileUtils.checkIfFileOrDirectoryExists(path)){
            return false;
        }
        String key = docId;
        if(newObject.containsKey("_id"))
            newObject.replace("_id",key);
        else
            newObject.put("_id",key);
        if(!DocumentsUtils.checkIfSchemaMatches(path+"/schema.json",newObject))
            return false;
        DeleteDocument delete = new DeleteDocument(dbName,collectionName,docId);
    System.out.println(newObject);
        return  delete.execute() && FileUtils.writeJsonToEndOfFile(newObject, path ,key);
    }
}
