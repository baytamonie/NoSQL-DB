package documents.functions;

import documents.entities.Packet;
import utils.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;

public class DeleteDataBase implements DatabaseWriteFunction{
    @Override
    public boolean execute(ObjectInputStream objectInputStream) {
        try {
            String dbName = ((Packet) objectInputStream.readObject()).getMessage();
            String path = "src/main/resources/databases/" + dbName;
            if (FileUtils.checkIfFileOrDirectoryExists(path)) {
                FileUtils.deleteDirectory(path);
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
}
