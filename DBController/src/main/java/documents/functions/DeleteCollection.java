package documents.functions;

import documents.entities.Packet;
import utils.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;

public class DeleteCollection implements DatabaseWriteFunction {

    @Override
    public boolean execute(ObjectInputStream objectInputStream) {
        try {
            String dbName = ((Packet) objectInputStream.readObject()).getMessage();
            String collectionName = ((Packet) objectInputStream.readObject()).getMessage();
            String path = "src/main/resources/databases/" + dbName + '/' + collectionName;
            if (FileUtils.checkIfFileOrDirectoryExists(path)) {
                FileUtils.deleteDirectory(path);
                return true;
            }
            return false;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
