package documents.functions;

import org.json.simple.JSONObject;
import utils.DocumentsUtils;
import utils.FileUtils;

public class AddDocumentToCollection implements DatabaseWriteFunction{

    private final String dbName;
    private final String collectionName;
    private final JSONObject objectToAdd;

    public AddDocumentToCollection(String dbName, String collectionName, JSONObject jsonObject) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.objectToAdd = jsonObject;
    }
    @Override
    public boolean execute() {

            String path = "src/main/resources/databases/" + dbName +'/' +collectionName;
            if(!FileUtils.checkIfFileOrDirectoryExists(path)){
                return false;
            }
            String key = DocumentsUtils.generateKey(dbName,collectionName);
            if(objectToAdd.containsKey("_id"))
                objectToAdd.replace("_id",key);
            else
                objectToAdd.put("_id",key);
            if(!DocumentsUtils.checkIfSchemaMatches(path+"/schema.json",objectToAdd))
                return false;

            return FileUtils.writeJsonToEndOfFile(objectToAdd, path ,key);

        }

}
