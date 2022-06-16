package documents.functions;

import documents.entities.Packet;
import org.json.simple.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AddDocumentToCollection implements DocumentWriteFunctions{
    @Override
    public boolean execute(ObjectInputStream clientInputStream, ObjectOutputStream clientOutputStream, ObjectInputStream controllerInputStream, ObjectOutputStream controllerOutputStream) {
        try{
            String dbName = ((Packet)clientInputStream.readObject()).getMessage();
            String collectionName = (((Packet) clientInputStream.readObject()).getMessage());
            JSONObject object = (JSONObject) clientInputStream.readObject();
            controllerOutputStream.writeObject(new Packet("addDocument"));
            controllerOutputStream.writeObject(new Packet(dbName));
            controllerOutputStream.writeObject(new Packet(collectionName));
            controllerOutputStream.writeObject(object);
            String didFunctionExecute = ((Packet)controllerInputStream.readObject()).getMessage();
            if(didFunctionExecute.equals("true")){
                clientOutputStream.writeObject(new Packet("true"));
                return true;
            }
            else{
                clientOutputStream.writeObject(new Packet("false"));
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
