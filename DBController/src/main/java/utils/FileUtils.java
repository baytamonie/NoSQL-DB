package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class FileUtils {

  private static volatile FileUtils fileUtils;
  private final IdsUtils idsUtils = IdsUtils.getInstance();

  private FileUtils() {}

  public static FileUtils getInstance() {
    FileUtils ref = fileUtils;
    if (ref == null) {
      synchronized (FileUtils.class) {
        ref = fileUtils;
        if (ref == null) fileUtils = ref = new FileUtils();
      }
    }
    return ref;
  }
  public String pathBuilder(String dbName, String collectionName, String fileName){

    return "src/main/resources/databases/"
            +dbName
            +'/'
            +collectionName
            +'/'
            +fileName;
  }
  public  boolean checkIfFileOrDirectoryExists(String path) {
    if (path == null) return false;
    File file = new File(path);
    return file.exists();
  }

  public  void makeDirectory(String path) {
    File file = new File(path);
    file.mkdir();
  }

  public  boolean createFile(String path) {
    File file = new File(path);
    try {
      file.createNewFile();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public  boolean deleteDirectory(String path) {
    try {
      deleteDirectoryRecursivelyJava(new File(path));
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private  void deleteDirectoryRecursivelyJava(File file) throws IOException {
    if (file.isDirectory()) {
      File[] entries = file.listFiles();
      if (entries != null) {
        for (File entry : entries) {
          deleteDirectoryRecursivelyJava(entry);
        }
      }
    }
    if (!file.delete()) {
      throw new IOException("Failed to delete " + file);
    }
  }

  public  boolean writeJsonToEndOfFile(JSONObject jsonObject, String path, String key) {
    String dataPath = path + "/data.json";
    String s = jsonObject.toString().replace("\\", "");
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(dataPath, "rw")) {
      long startOfNewObject = randomAccessFile.length();
      randomAccessFile.seek(randomAccessFile.length());
      randomAccessFile.writeBytes(s);
      long endOfNewObject = randomAccessFile.length();
      if (!idsUtils.updateIdsFile(path, key, startOfNewObject, endOfNewObject)) return false;
      if (randomAccessFile != null) randomAccessFile.close();
      return true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public  JSONObject getObjectRandomAccessFile(String path, long startIndex, long endIndex) {
    JSONParser jsonParser = new JSONParser();
    int sizeOfBytes = (int) (endIndex - startIndex);
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r")) {
      randomAccessFile.seek(startIndex);
      byte[] bytes = new byte[(int) sizeOfBytes];
      randomAccessFile.read(bytes);
      String s = new String(bytes);
      JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
      randomAccessFile.close();
      return jsonObject;
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public  boolean writeToFile(String path, Object objectToWrite) {
    try {
      FileWriter fileWriter = new FileWriter(path);
      fileWriter.write(objectToWrite.toString());
      fileWriter.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

}
