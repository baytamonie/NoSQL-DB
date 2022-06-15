package documents.functions;

import documents.entities.Packet;
import utils.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;

public class CreateDataBase implements DatabaseWriteFunction{

    @Override
    public boolean execute(ObjectInputStream objectInputStream){

        try {
            String dbName = ((Packet) objectInputStream.readObject()).getMessage();
            String path = "src/main/resources/databases/"+dbName;
            FileUtils.makeDirectory(path);
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
