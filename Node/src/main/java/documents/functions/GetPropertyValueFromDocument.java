package documents.functions;

import documents.IdsObject;
import documents.entities.Packet;
import org.json.simple.JSONObject;
import utilities.DocumentUtils;
import utilities.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class GetPropertyValueFromDocument implements DocumentFunction {
    @Override
    public boolean execute(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        try {
            String dbName = ((Packet)inputStream.readObject()).getMessage();
            String collectionName = ((Packet)inputStream.readObject()).getMessage();
            String documentId = ((Packet) inputStream.readObject()).getMessage();
            String propertyName = ((Packet) inputStream.readObject()).getMessage();
            if (!DocumentUtils.checkIfCollectionExists(dbName, collectionName))
                return false;
                String idsDocPath =
                        DocumentUtils.pathBuilder(dbName, collectionName, "ids.json");
                HashMap<String, IdsObject> ids = FileUtils.loadIdsJSON(idsDocPath);
                String docPath =
                        DocumentUtils.pathBuilder(
                                dbName, collectionName, "data.json");
                IdsObject idsObject = ids.get(documentId);
                if(idsObject==null)
                    return false;
                JSONObject jsonObject =
                        FileUtils.getObjectRandomAccessFile(docPath, idsObject.getBegin(), idsObject.getEnd());
                if(jsonObject==null)
                    return false;
                Object obj = jsonObject.get(propertyName);
                outputStream.writeObject(obj);
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
