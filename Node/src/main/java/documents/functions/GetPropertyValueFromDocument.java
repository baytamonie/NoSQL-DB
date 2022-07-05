package documents.functions;

import cache.LRUCache;
import documents.IdsObject;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.json.simple.JSONObject;
import utilities.DocumentUtils;
import utilities.FileUtils;

import javax.print.Doc;
import java.util.HashMap;

public class GetPropertyValueFromDocument implements DocumentReadFunctions {

  private final String dbName;
  private final String collectionName;
  private final String documentId;
  private final String propertyName;

  public GetPropertyValueFromDocument(
      String dbName, String collectionName, String documentId, String propertyName) {
    this.dbName = dbName;
    this.collectionName = collectionName;
    this.documentId = documentId;
    this.propertyName = propertyName;
  }

  @Override
  public Object execute() {
    if(lruCache.get(documentId)!=null)
    {
      if(!documentsUtils.checkIfPropertyInSchema(propertyName,documentsUtils.pathBuilder(dbName,collectionName,"schema.json")))
        return null;
      return lruCache.get(documentId).get(propertyName);
    }

    if (!documentsUtils.checkIfCollectionExists(dbName, collectionName)) return null;
    if(!documentsUtils.checkIfPropertyInSchema(propertyName,documentsUtils.pathBuilder(dbName,collectionName,"schema.json")))
      return null;
    String idsDocPath = documentsUtils.pathBuilder(dbName, collectionName, "ids.json");
    HashMap<String, IdsObject> ids = fileUtils.loadIdsJSON(idsDocPath);
    String docPath = documentsUtils.pathBuilder(dbName, collectionName, "data.json");
    IdsObject idsObject;
    try{
       idsObject = ids.get(documentId);
    }catch (NullPointerException e){
      e.printStackTrace();
      return null;
    }
    JSONObject jsonObject =
            fileUtils.getObjectRandomAccessFile(docPath, idsObject.getBegin(), idsObject.getEnd());
    lruCache.put(documentId,jsonObject);
    if (jsonObject == null) return null;

    return jsonObject.get(propertyName);
  }


}
