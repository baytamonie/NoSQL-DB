package documents.functions;

import documents.entities.Packet;
import documents.entities.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import server.Server;

public class Login implements DocumentReadFunctions {

  private final String username;
  private final String password;

  public Login(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public Object execute() {

    for (User user : Server.userList) {
      if (user.getName().equals(username) && user.getPassword().equals(password)) {
        Packet msg = new Packet("true\n" + user.getAuthority());
        return msg;
      }
    }
    Packet msg = new Packet("false");
    return msg;
    }
}
