package documents.functions;

import documents.entities.Packet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CreateDatabase implements DocumentWriteFunctions {

    @Override
    public boolean execute(ObjectInputStream clientInputStream, ObjectOutputStream clientOutputStream, ObjectInputStream controllerInputStream, ObjectOutputStream controllerOutputStream) {
        try{
            String dbName = ((Packet)clientInputStream.readObject()).getMessage();
            controllerOutputStream.writeObject(new Packet("createDatabase"));
            controllerOutputStream.writeObject(new Packet(dbName));
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
