package documents.functions;

import com.google.common.collect.LinkedHashMultimap;
import documents.IdsObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import utilities.DocumentUtils;
import utilities.FileUtils;

import javax.print.Doc;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GetAllDocumentsWithProperty implements DocumentReadFunctions {

  private final String dbName;
  private final String collectionName;
  private final String propertyName;
  private final String propertyValue;

  public GetAllDocumentsWithProperty(
      String dbName, String collectionName, String propertyName, String propertyValue) {
    this.dbName = dbName;
    this.collectionName = collectionName;
    this.propertyName = propertyName;
    this.propertyValue = propertyValue;
  }

  @Override
  public Object execute() {
    if (!documentsUtils.checkIfCollectionExists(dbName, collectionName)) return null;
    String path = "src/main/resources/databases/" + dbName + '/' + collectionName + "/schema.json";
    if (!documentsUtils.checkIfPropertyInSchema(propertyName, path)) return null;
    if (fileUtils.checkIfPropertyIsIndexed(dbName, collectionName, propertyName)) {
      String indexPath =
          "src/main/resources/databases/"
              + dbName
              + '/'
              + collectionName
              + "/"
              + propertyName
              + ".json";
      System.out.println("returned from index");

      return getArray(indexPath,propertyValue);
      }


    String idsDocPath = documentsUtils.pathBuilder(dbName, collectionName, "ids.json");
    JSONArray jsonArray = new JSONArray();
    HashMap<String, IdsObject> ids = fileUtils.loadIdsJSON(idsDocPath);
    if (ids == null) return null;
    String dataPath = documentsUtils.pathBuilder(dbName, collectionName, "data.json");
    for (IdsObject idsObject : ids.values()) {
      JSONObject jsonObject =
          fileUtils.getObjectRandomAccessFile(dataPath, idsObject.getBegin(), idsObject.getEnd());
      if (jsonObject != null && jsonObject.get(propertyName).equals(propertyValue))
        jsonArray.add(jsonObject);
    }

    return jsonArray;
  }

  private JSONArray getArray(String path,String propertyValue) {
    JSONArray ans = new JSONArray();
    try (FileReader fileReader = new FileReader(path)) {
      JSONParser jsonParser = new JSONParser();
      JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
      for (Object k : jsonObject.keySet()) {
        try {
          System.out.println(k);
          System.out.println(propertyValue);
          if(propertyValue.equals(k)){
          JSONArray jsonArray = (JSONArray) jsonObject.get(k);
          for (Object ob : jsonArray) {
            String key = ob.toString();

            GetDocumentById getDocumentById = new GetDocumentById(dbName,collectionName,key);
            ans.add(getDocumentById.execute());
          }
          return ans;
          }
        } catch (Exception e) {
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }


    return ans;
  }}
