package documents.functions;

import org.json.simple.JSONObject;
import utils.FileUtils;

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
  public boolean execute() {

      String path = "src/main/resources/databases/" + dbName;
      if (FileUtils.checkIfFileOrDirectoryExists(path)) {
        path = path + '/' + collectionName;
        if(!FileUtils.checkIfFileOrDirectoryExists(path))
          return createCollectionFiles(path, schema);
        return true;
      }
      return false;

  }

  private boolean createCollectionFiles(String path,JSONObject schema){
    FileUtils.makeDirectory(path);
    FileUtils.createFile(path+'/'+"data.json");
    FileUtils.createFile(path+'/'+"ids.json");
    FileUtils.createFile(path+'/'+"schema.json");
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(path+'/'+"schema.json","rw")){
      randomAccessFile.writeBytes(schema.toJSONString());
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}
