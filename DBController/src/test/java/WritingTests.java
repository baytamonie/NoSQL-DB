import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WritingTests {
//  @Test
//  public void writeToEndOfTheFile(){
//    String path = "src/main/resources/databases/TestDatabase/TestCollection/data.json";
//    try(RandomAccessFile randomAccessFile = new RandomAccessFile(path,"rw")){
//      JSONObject jsonObject = new JSONObject();
//      jsonObject.put("name","ahmad");
//      jsonObject.put("age",20);
//      System.out.println(randomAccessFile.getFilePointer());
//      System.out.println(randomAccessFile.length());
//      randomAccessFile.seek(randomAccessFile.length());
//      randomAccessFile.writeBytes(jsonObject.toJSONString());
//      randomAccessFile.close();
//    } catch (FileNotFoundException e) {
//      throw new RuntimeException(e);
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//  }
  @Test
  public void deleteData(){
        String path = "src/main/resources/databases/TestDatabase/TestCollection/data.json";

    try(RandomAccessFile randomAccessFile = new RandomAccessFile(path,"rw")){
      randomAccessFile.setLength(0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

//
//@Test
//  public void writeToRAF() {
//    JSONObject jsonObject = new JSONObject();
//    jsonObject.put("name", "Yousef");
//    jsonObject.put("age", 20);
//    System.out.println(jsonObject.toJSONString());
//    String path = "src/main/resources/databases/Employees/Managers/schema.json";
//    try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw")) {
//
//      randomAccessFile.writeBytes(jsonObject.toJSONString());
//
//    } catch (FileNotFoundException e) {
//      throw new RuntimeException(e);
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//  }
}
