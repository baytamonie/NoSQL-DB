package documents.functions;

import documents.IdsObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class GetCollectionFromDatabase implements DocumentReadFunctions {

  private final String dbName;
  private final String collectionName;

  public GetCollectionFromDatabase(String dbName, String collectionName) {
    this.dbName = dbName;
    this.collectionName = collectionName;
  }

  @Override
  public Object execute() {

    if (!documentsUtils.checkIfCollectionExists(dbName, collectionName)) return null;
    String idsDocPath = documentsUtils.pathBuilder(dbName, collectionName, "ids.json");
    JSONArray jsonArray = new JSONArray();
    HashMap<String, IdsObject> ids = fileUtils.loadIdsJSON(idsDocPath);
    if (ids == null) return null;
    String dataPath = documentsUtils.pathBuilder(dbName, collectionName, "data.json");
    for (IdsObject idsObject : ids.values()) {
      JSONObject jsonObject =
              fileUtils.getObjectRandomAccessFile(dataPath, idsObject.getBegin(), idsObject.getEnd());
      if (jsonObject != null) jsonArray.add(jsonObject);
    }

    return jsonArray;
  }
}
