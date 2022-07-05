package utils;

import com.google.common.collect.LinkedHashMultimap;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class IndexingUtils {

  private static volatile IndexingUtils indexingUtils;
  private final FileUtils fileUtils = FileUtils.getInstance();

  private IndexingUtils() {}

  public static IndexingUtils getInstance() {
    IndexingUtils ref = indexingUtils;
    if (ref == null) {
      synchronized (IndexingUtils.class) {
        ref = indexingUtils;
        if (ref == null) indexingUtils = ref = new IndexingUtils();
      }
    }
    return ref;
  }

  private LinkedHashMultimap<Object,Object> getMap(File file){
    try (FileReader fileReader = new FileReader(file)) {
      LinkedHashMultimap<Object, Object> map = LinkedHashMultimap.create();
      JSONParser jsonParser = new JSONParser();
      JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
      for (Object k : jsonObject.keySet()) {
        try {
          JSONArray jsonArray = (JSONArray) jsonObject.get(k);
          for (Object ob : jsonArray) {
            if (ob != null) map.put(k, ob);
          }
        } catch (Exception e) {
          map.put(k, jsonObject.get(k));
        }
      }
      return map;
    } catch (IOException | ParseException e) {
      e.printStackTrace();
      return null;
    }

  }

  public void deleteObjectFromIndexedFiles(JSONObject objectToDelete, String dbName, String collectionName){
    List<File> files;
    files = returnIndexedFiles(dbName, collectionName);
    if (files.size() != 0) {
      for (File file : files) {
          String path = file.getPath();
        LinkedHashMultimap<Object, Object> map = getMap(file);
        String fileNameWithoutExtension =
                  file.getName().substring(0, file.getName().lastIndexOf('.'));
        if (map != null) {
          map.remove(objectToDelete.get(fileNameWithoutExtension),objectToDelete.get("_id"));
        }

        Gson gson = new Gson();
        String jsonString;
        if (map != null) {
          jsonString = gson.toJson(map.asMap());
          fileUtils.writeToFile(path, jsonString);
        }
      }
    }
  }
  public void addObjectToIndexedFiles(JSONObject objectToAdd, String dbName, String collectionName) {
    List<File> files;
    files = returnIndexedFiles(dbName, collectionName);
    if (files.size() != 0) {
      for (File file : files) {
        String path = file.getPath();
        LinkedHashMultimap<Object, Object> map = getMap(file);
        String fileNameWithoutExtension =
                file.getName().substring(0, file.getName().lastIndexOf('.'));
        if (map != null) {
          map.put(objectToAdd.get(fileNameWithoutExtension),objectToAdd.get("_id"));
        }
        Gson gson = new Gson();
        String jsonString;
        if (map != null) {
          jsonString = gson.toJson(map.asMap());
          fileUtils.writeToFile(path, jsonString);
        }
      }
    }
  }

  private  List<File> returnIndexedFiles(String dbName, String collectionName){
    String path = "src/main/resources/databases/"+dbName+'/'+collectionName;
    File file = new File(path);
    List<File> indexedFileList = new LinkedList<>();
    for (File f : Objects.requireNonNull(file.listFiles())){
      if(f.getName().equals("data.json")||f.getName().equals("schema.json")||f.getName().equals("index.txt")||f.getName().equals("ids.json"))
        continue;
      indexedFileList.add(f);
    }
    return indexedFileList;
  }
}
