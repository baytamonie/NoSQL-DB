package documents.functions;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CreateCollection implements DatabaseWriteFunction {

  private final String dbName;
  private final String collectionName;
  private final JSONObject schema;

  public CreateCollection(String dbName, String collectionName, JSONObject schema) {
    this.dbName = dbName;
    this.collectionName = collectionName;
    this.schema = schema;
  }

  @Override
  public synchronized boolean execute() {

      String path = "src/main/resources/databases/" + dbName;
      if (fileUtils.checkIfFileOrDirectoryExists(path)) {
        path = path + '/' + collectionName;
        if(!fileUtils.checkIfFileOrDirectoryExists(path))
          return createCollectionFiles(path, schema);
        return true;
      }
      return false;

  }

  private  boolean createCollectionFiles(String path,JSONObject schema){
    fileUtils.makeDirectory(path);
    fileUtils.createFile(path+'/'+"data.json");
    fileUtils.createFile(path+'/'+"ids.json");
    fileUtils.createFile(path+'/'+"schema.json");
    fileUtils.createFile(path+'/'+"index.txt");
    fileUtils.writeToFile(path+"/index.txt",0);

    try (RandomAccessFile randomAccessFile = new RandomAccessFile(path+'/'+"schema.json","rw")){
      randomAccessFile.writeBytes(schema.toJSONString());
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}
