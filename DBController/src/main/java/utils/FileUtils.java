package utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class FileUtils {

  public static boolean checkIfFileOrDirectoryExists(String path) {
    if (path == null) return false;
    File file = new File(path);
    return file.exists();
  }

  public static void makeDirectory(String path) {
    File file = new File(path);
    file.mkdir();
  }

  public static boolean createFile(String path) {
    File file = new File(path);
    try {
      file.createNewFile();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean deleteDirectory(String path) {
    try {
      deleteDirectoryRecursionJava(new File(path));
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private static void deleteDirectoryRecursionJava(File file) throws IOException {
    if (file.isDirectory()) {
      File[] entries = file.listFiles();
      if (entries != null) {
        for (File entry : entries) {
          deleteDirectoryRecursionJava(entry);
        }
      }
    }
    if (!file.delete()) {
      throw new IOException("Failed to delete " + file);
    }
  }
  private static boolean updateIdsFile(String path, String key,long begin, long end){
    path = path+"/ids.json";
    File file = new File(path);
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(path);
      JSONParser jsonParser = new JSONParser();
      JSONArray jsonArray = new JSONArray();
      if(file.length()!=0)
      {
         jsonArray = (JSONArray) jsonParser.parse(fileReader);

      }

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("_id",key);
      jsonObject.put("begin",begin);
      jsonObject.put("end",end);
      jsonArray.add(jsonObject);
      FileWriter fileWriter = new FileWriter(path);
      fileWriter.write(String.valueOf(jsonArray));
      fileWriter.close();

      return true;
    } catch (IOException | ParseException e) {
      e.printStackTrace();
      return false;
    }
  }



  public static boolean writeJsonToEndOfFile(JSONObject jsonObject, String path,String key) {
    String dataPath = path+"/data.json";
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(dataPath, "rw")) {
      long startOfNewObject = randomAccessFile.length();
      randomAccessFile.seek(randomAccessFile.length());
      randomAccessFile.writeBytes(jsonObject.toJSONString());
      long endOfNewObject = randomAccessFile.length();
      if(updateIdsFile(path,key,startOfNewObject,endOfNewObject))
        return false;

      if(randomAccessFile!=null)
        randomAccessFile.close();
      return true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}
