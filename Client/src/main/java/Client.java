import driver.ClientConnector;
import driver.ClientHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.List;

public class Client {

  public static void main(String[] args) {

    ClientHandler clientHandler = new ClientHandler();
    clientHandler.createConnection();
    clientHandler.login("bayta", "123");
    System.out.println(clientHandler.getDocument("films", "dockerVideos", "films.dockerVideos.0"));
    System.out.println(clientHandler.getDocument("films", "dockerVideos", "films.dockerVideos.1"));
//    JSONArray jsonArray = clientHandler.getCollection("videos", "dockerVideos");
//    for (Object obj : jsonArray) {
//      System.out.println(((JSONObject) obj).toJSONString());
//    }
//    Object obj = clientHandler.getProperty("database1","collection1","129124","name");
//    System.out.println(obj.toString());
//  clientHandler.createDatabase("films");
//  JSONObject schema = new JSONObject();
//  schema.put("name","string");
//  schema.put("url","string");
//  clientHandler.createCollection("films","dockerVideos",schema);
//  JSONObject jsonObject = new JSONObject();
//  jsonObject.put("name","rabbits");
//  jsonObject.put("url","youtube.com/rabbits");
//  clientHandler.writeDocument("films","dockerVideos",jsonObject);
  }
}
