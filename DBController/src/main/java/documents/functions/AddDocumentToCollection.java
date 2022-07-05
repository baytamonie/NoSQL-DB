package documents.functions;

import org.json.simple.JSONObject;


public class AddDocumentToCollection implements DatabaseWriteFunction {

  private final String dbName;
  private final String collectionName;
  private final JSONObject objectToAdd;
  public AddDocumentToCollection(String dbName, String collectionName, JSONObject document) {
    this.dbName = dbName;
    this.collectionName = collectionName;
    this.objectToAdd = document;
  }

  @Override
  public synchronized boolean execute() {

    String path = "src/main/resources/databases/" + dbName + '/' + collectionName;
    if (!fileUtils.checkIfFileOrDirectoryExists(path)) {
      return false;
    }

    String key = documentsUtils.generateKey(dbName, collectionName);

    if (!documentsUtils.checkIfSchemaMatches(path + "/schema.json", objectToAdd)) return false;
    objectToAdd.put("_id", key);
    indexingUtils.addObjectToIndexedFiles(objectToAdd,dbName,collectionName);
    return fileUtils.writeJsonToEndOfFile(objectToAdd, path, key);
  }


}
