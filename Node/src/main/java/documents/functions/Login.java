package documents.functions;

import documents.entities.Packet;
import documents.entities.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import server.Server;
public class Login implements DocumentReadFunctions {

    @Override
    public boolean execute(ObjectInputStream inputStream, ObjectOutputStream outputStream) {

        try {
            String username = ((Packet) inputStream.readObject()).getMessage();
            String password = ((Packet) inputStream.readObject()).getMessage();
            for (User user : Server.userList) {
                if (user.getName().equals(username) && user.getPassword().equals(password)) {
                    outputStream.writeObject(new Packet("true"));
                    outputStream.writeObject(new Packet(user.getAuthority()));
                    System.out.println("successful login");
                    return true;
                }
            }
            outputStream.writeObject(new Packet("false"));
            System.out.println("not successful login");
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
