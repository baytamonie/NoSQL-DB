package documents.functions;

import com.google.common.collect.LinkedHashMultimap;
import com.google.gson.Gson;
import documents.entities.IdsObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.DocumentsUtils;
import utils.FileUtils;

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
  public boolean execute() {
    String path = "src/main/resources/databases/" + dbName + '/' + collectionName;
    if (!FileUtils.checkIfFileOrDirectoryExists(path)) return false;
    if (!DocumentsUtils.checkIfPropertyInSchema(propertyName, path + "/schema.json")) return false;
    LinkedHashMultimap<Object, Object> map = LinkedHashMultimap.create();
    if (!FileUtils.createFile(path + '/' + propertyName + ".json")) return false;

    String idsDocPath = path+"/ids.json";
    HashMap<String, IdsObject> ids = FileUtils.loadIdsJSON(idsDocPath);
    if (ids == null) return false;
    String dataPath = path+"/data.json";
    for (IdsObject idsObject : ids.values()) {
      JSONObject jsonObject =
              FileUtils.getObjectRandomAccessFile(dataPath, idsObject.getBegin(), idsObject.getEnd());
      if (jsonObject != null) {
        map.put(jsonObject.get(propertyName), jsonObject.get("_id"));

      }
    }
    Gson gson = new Gson();

    String jsonString = gson.toJson(map.asMap());
    JSONArray jsonArray = new JSONArray();
    System.out.println(jsonString);
    String newIndexPath = path + '/' + propertyName + ".json";
    FileUtils.writeToFile(newIndexPath,jsonString);
    return true;
  }
}
