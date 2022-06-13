package documents.functions;

import documents.entities.Packet;
import org.json.simple.JSONArray;
import utilities.DocumentUtils;
import utilities.FileHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GetCollectionFromDatabase implements DocumentFunction {
    @Override
    public boolean execute(ObjectInputStream inputStream, ObjectOutputStream outputStream) {

        try {
            String dbName = ((Packet) inputStream.readObject()).getMessage();
            String collectionName = ((Packet) inputStream.readObject()).getMessage();
            if (DocumentUtils.checkIfCollectionExists(dbName, collectionName))
                return false;
            String dataPath =
                    DocumentUtils.pathBuilder(
                            dbName, collectionName, "data.json");
            JSONArray jsonArray = FileHandler.loadData(dataPath);
            if (jsonArray == null)
                return false;
            outputStream.writeObject(jsonArray);
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
