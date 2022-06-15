package documents.functions;

import documents.entities.Packet;
import org.json.simple.JSONObject;
import utils.DocumentsUtils;
import utils.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;

public class AddDocumentToCollection implements DatabaseWriteFunction{
    @Override
    public boolean execute(ObjectInputStream objectInputStream) {
        try {
            String dbName = ((Packet) objectInputStream.readObject()).getMessage();
            String collectionName = ((Packet) objectInputStream.readObject()).getMessage();
            JSONObject objectToAdd = (JSONObject) objectInputStream.readObject();
            String key = DocumentsUtils.generateKey(dbName,collectionName);
            if(objectToAdd.containsKey("_id"))
                objectToAdd.replace("_id",key);
            else
                objectToAdd.put("_id",key);
            String path = "src/main/resources/databases/" + dbName +'/' +collectionName;
            if(!FileUtils.checkIfFileOrDirectoryExists(path)){
                return false;
            }
            if(!DocumentsUtils.checkIfSchemaMatches(path+"/schema.json",objectToAdd))
                return false;

            if(!FileUtils.writeJsonToEndOfFile(objectToAdd,path+"/data.json"))
                return false;

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }
}
