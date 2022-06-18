package documents.functions;

import documents.IdsObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utilities.DocumentUtils;
import utilities.FileUtils;

import java.util.HashMap;

public class GetAllDocumentsWithProperty implements DocumentReadFunctions {

    private final String dbName;
    private final String collectionName;
    private final String propertyName;
    private final String propertyValue;
    public GetAllDocumentsWithProperty(String dbName, String collectionName, String propertyName, String propertyValue) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    @Override
    public Object execute() {
        if (!DocumentUtils.checkIfCollectionExists(dbName, collectionName))
            return null;
        String path = "src/main/resources/databases/"+dbName+'/'+collectionName+"/schema.json";
        if(!DocumentUtils.checkIfPropertyInSchema(propertyName,path))
            return null;
        String idsDocPath = DocumentUtils.pathBuilder(dbName, collectionName, "ids.json");
        JSONArray jsonArray = new JSONArray();
        HashMap<String, IdsObject> ids = FileUtils.loadIdsJSON(idsDocPath);
        if (ids == null) return null;
        String dataPath = DocumentUtils.pathBuilder(dbName, collectionName, "data.json");
        for (IdsObject idsObject : ids.values()) {
            JSONObject jsonObject =
                    FileUtils.getObjectRandomAccessFile(dataPath, idsObject.getBegin(), idsObject.getEnd());
            if (jsonObject != null && jsonObject.get(propertyName).equals(propertyValue))
                jsonArray.add(jsonObject);
        }

        return jsonArray;
    }
}
