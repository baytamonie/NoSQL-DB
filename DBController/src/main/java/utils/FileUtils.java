package utils;

import org.json.simple.JSONObject;

import java.io.*;
import java.util.Scanner;

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

  public static boolean writeJsonToEndOfFile(JSONObject jsonObject, String path) {

    try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw")) {
      randomAccessFile.seek(randomAccessFile.length());
      randomAccessFile.writeBytes(jsonObject.toJSONString());
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
