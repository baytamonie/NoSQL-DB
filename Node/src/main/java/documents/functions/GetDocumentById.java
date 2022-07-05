package documents.functions;

import documents.IdsObject;
import documents.entities.Packet;
import org.json.simple.JSONObject;
import utilities.DocumentUtils;
import utilities.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class GetDocumentById implements DocumentReadFunctions {

  private final String dbName;
  private final String collectionName;
  private final String documentId;

  public GetDocumentById(String dbName, String collectionName, String documentId) {
    this.dbName = dbName;
    this.collectionName = collectionName;
    this.documentId = documentId;

  }

  @Override
  public Object execute() {

    if (lruCache.get(documentId) != null) {
      System.out.println("document found in cache");
      return lruCache.get(documentId);
    }

    if (!documentsUtils.checkIfCollectionExists(dbName, collectionName)) return null;

    String idsDocPath = documentsUtils.pathBuilder(dbName, collectionName, "ids.json");
    HashMap<String, IdsObject> ids = fileUtils.loadIdsJSON(idsDocPath);
    IdsObject idsObject = ids.get(documentId);
    if (idsObject == null) return null;
    String dataPath = documentsUtils.pathBuilder(dbName, collectionName, "data.json");
    JSONObject jsonObject =
            fileUtils.getObjectRandomAccessFile(dataPath, idsObject.getBegin(), idsObject.getEnd());
    lruCache.put(documentId,jsonObject);
    return jsonObject;
  }
}
