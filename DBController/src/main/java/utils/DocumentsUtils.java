package utils;

import documents.entities.JSONSchema;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

public class DocumentsUtils {

  private static volatile DocumentsUtils documentsUtils;
  public static DocumentsUtils getInstance() {
    DocumentsUtils ref = documentsUtils;
    if (ref == null) {
      synchronized (DocumentsUtils.class) {
        ref = documentsUtils;
        if (ref == null) documentsUtils = ref = new DocumentsUtils();
      }
    }
    return ref;
  }

  private DocumentsUtils(){

  }
  public  boolean checkIfPropertyInSchema(String propertyName, String path) {
    try (FileReader fileReader = new FileReader(path)) {
      JSONParser jsonParser = new JSONParser();
      JSONObject schema = (JSONObject) (jsonParser.parse(fileReader));
      fileReader.close();
      return schema != null && schema.containsKey(propertyName);
    } catch (IOException | ParseException e) {
      e.printStackTrace();
      return false;
    }
  }

  public  boolean checkIfSchemaMatches(String path, JSONObject jsonObject) {
    JSONParser jsonParser = new JSONParser();
    FileReader fileReader;
    try {
      fileReader = new FileReader(path);
      JSONObject schema = (JSONObject) (jsonParser.parse(fileReader));
      Iterator<String> schemaKeys = schema.keySet().iterator();
      Iterator<String> jsonObjectKeys = schema.keySet().iterator();
      int numOfJsonObjectKeys = 0;
      if(jsonObject.get("_id")!=null)
        jsonObject.remove("_id");
      while (jsonObjectKeys.hasNext()) {
        jsonObjectKeys.next();
        numOfJsonObjectKeys++;
      }
      JSONSchema jsonSchema = new JSONSchema(schema);
      int numOfItemsInSchema = 0;
      while (schemaKeys.hasNext()) {
        String key = schemaKeys.next();
        if (key.equals("_id")) continue;
        if (jsonObject.containsKey(key)
            && jsonObject.get(key).getClass().equals(jsonSchema.getPropertyType(key))) {
          numOfItemsInSchema++;
        } else return false;
      }
      return numOfItemsInSchema == numOfJsonObjectKeys;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public  String generateKey(String dbName, String collectionName) {
    String key = dbName + '.' + collectionName + '.';
    String pointerPath =
        "src/main/resources/databases/" + dbName + '/' + collectionName + '/' + "index.txt";
    try (FileInputStream fileInputStream = new FileInputStream(pointerPath)) {
      Scanner scanner = new Scanner(fileInputStream, "UTF-8");
      String line = scanner.nextLine();
      writeNewKey(line, pointerPath);
      key = key + line;
      fileInputStream.close();
      scanner.close();
      return key;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private  void writeNewKey(String key, String path) {
    try {
      Long keyValue = Long.valueOf(key);
      keyValue++;
      FileWriter fileWriter = new FileWriter(path);
      fileWriter.write(String.valueOf(keyValue));
      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
