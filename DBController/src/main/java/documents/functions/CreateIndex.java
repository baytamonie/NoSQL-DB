package documents.functions;

import com.google.common.collect.LinkedHashMultimap;
import com.google.gson.Gson;
import documents.entities.IdsObject;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class CreateIndex implements DatabaseWriteFunction {

  private final String dbName;
  private final String collectionName;
  private final String propertyName;

  public CreateIndex(String dbName, String collectionName, String propertyName) {
    this.dbName = dbName;
    this.collectionName = collectionName;
    this.propertyName = propertyName;
  }

  @Override
  public synchronized boolean execute() {
    String path = "src/main/resources/databases/" + dbName + '/' + collectionName;
    if (!fileUtils.checkIfFileOrDirectoryExists(path)) return false;
    if (!documentsUtils.checkIfPropertyInSchema(propertyName, path + "/schema.json")) return false;
    LinkedHashMultimap<Object, Object> map = LinkedHashMultimap.create();
    if (!fileUtils.createFile(path + '/' + propertyName + ".json")) return false;

    String idsDocPath = path+"/ids.json";
    HashMap<String, IdsObject> ids = idsUtils.loadIdsJSON(idsDocPath);
    if (ids == null) return false;
    String dataPath = path+"/data.json";
    for (IdsObject idsObject : ids.values()) {
      JSONObject jsonObject =
              fileUtils.getObjectRandomAccessFile(dataPath, idsObject.getBegin(), idsObject.getEnd());
      if (jsonObject != null) {
        map.put(jsonObject.get(propertyName), jsonObject.get("_id"));
      }
    }
    Gson gson = new Gson();
    String jsonString = gson.toJson(map.asMap());
    String newIndexPath = path + '/' + propertyName + ".json";
    documentsUtils.generateKey(dbName,collectionName);
    return fileUtils.writeToFile(newIndexPath,jsonString);
  }
}
