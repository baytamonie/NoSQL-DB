package documents.functions;

import documents.entities.Packet;
import org.json.simple.JSONObject;
import utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;

public class CreateCollection implements DatabaseWriteFunction {

  @Override
  public boolean execute(ObjectInputStream objectInputStream) {
    try {
      String dbName = ((Packet) objectInputStream.readObject()).getMessage();
      String collectionName = ((Packet) objectInputStream.readObject()).getMessage();
      String path = "src/main/resources/databases/" + dbName;
      JSONObject schema = (JSONObject) objectInputStream.readObject();
      if (FileUtils.checkIfFileOrDirectoryExists(path)) {
        path = path + '/' + collectionName;
        if(!FileUtils.checkIfFileOrDirectoryExists(path))
          if(!createCollectionFiles(path,schema))
            return false;
        return true;
      }
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean createCollectionFiles(String path,JSONObject schema){
    FileUtils.makeDirectory(path);
    FileUtils.createFile(path+'/'+"data.json");
    FileUtils.createFile(path+'/'+"ids.json");
    FileUtils.createFile(path+'/'+"schema.json");
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(path+'/'+"schema.json","rw")){
      randomAccessFile.writeBytes(schema.toJSONString());
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
