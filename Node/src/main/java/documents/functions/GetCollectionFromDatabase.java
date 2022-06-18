package documents.functions;

import documents.IdsObject;
import documents.entities.Packet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utilities.DocumentUtils;
import utilities.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    if (!DocumentUtils.checkIfCollectionExists(dbName, collectionName)) return null;
    String idsDocPath = DocumentUtils.pathBuilder(dbName, collectionName, "ids.json");
    JSONArray jsonArray = new JSONArray();
    HashMap<String, IdsObject> ids = FileUtils.loadIdsJSON(idsDocPath);
    if (ids == null) return null;
    String dataPath = DocumentUtils.pathBuilder(dbName, collectionName, "data.json");
    for (IdsObject idsObject : ids.values()) {
      JSONObject jsonObject =
          FileUtils.getObjectRandomAccessFile(dataPath, idsObject.getBegin(), idsObject.getEnd());
      if (jsonObject != null) jsonArray.add(jsonObject);
    }

    return jsonArray;
  }
}
