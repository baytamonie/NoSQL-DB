package documents.functions;

import documents.entities.Packet;
import org.json.simple.JSONArray;
import utilities.DocumentUtils;
import utilities.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GetCollectionFromDatabase implements DocumentReadFunctions {

  private final String dbName;
  private final String collectionName;

  public GetCollectionFromDatabase(String dbName, String collectionName) {
    this.dbName = dbName;
    this.collectionName = collectionName;
  }

  @Override
  public Object execute() {

    if (!DocumentUtils.checkIfCollectionExists(dbName, collectionName))
      return null;
    String dataPath = DocumentUtils.pathBuilder(dbName, collectionName, "data.json");
    JSONArray jsonArray = FileUtils.loadData(dataPath);
    return jsonArray;
  }
}
