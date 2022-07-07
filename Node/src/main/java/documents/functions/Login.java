package documents.functions;

import documents.entities.Packet;
import documents.entities.User;
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
        return new Packet("true\n" + user.getAuthority());
      }
    }
    return new Packet("false");
    }
}
