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
    clientHandler.createIndex("createIndexTest","test1","name");
    //        System.out.println(clientHandler.getDocument("films", "meta", "films.meta.0"));
    //        System.out.println(clientHandler.getDocument("films", "meta", "films.meta.1"));
    //    JSONArray jsonArray = clientHandler.getCollection("films", "meta");
    //    for (Object obj : jsonArray) {
    //      System.out.println(((JSONObject) obj).toJSONString());
    //    }
    //        JSONArray jsonArray2 = clientHandler.getProperties("films","meta","name","cats");
    //            for (Object obj : jsonArray2) {
    //      System.out.println(((JSONObject) obj).toJSONString());
    //    }
    //    Object obj = clientHandler.getProperty("database1","collection1","129124","name");
    //    System.out.println(obj.toString());
    //  clientHandler.createDatabase("createIndexTest");
    //  JSONObject schema = new JSONObject();
    //  schema.put("name","string");
    //  schema.put("age","integer");
    //  clientHandler.createCollection("createIndexTest","test2",schema);
//    JSONObject jsonObject = new JSONObject();
//    jsonObject.put("name", "ahmad");
//    jsonObject.put("age", 10);
//    JSONObject jsonObject2 = new JSONObject();
//    jsonObject2.put("name", "omar");
//    jsonObject2.put("age", 15);
//    JSONObject jsonObject3 = new JSONObject();
//    jsonObject3.put("name", "ahmad");
//    jsonObject3.put("age", 20);
//    JSONObject jsonObject4 = new JSONObject();
//    jsonObject4.put("name", "yousf");
//    jsonObject4.put("age", 10);
//    JSONObject jsonObject5 = new JSONObject();
//    jsonObject5.put("name", "tala");
//    jsonObject5.put("age", 15);
//    JSONObject jsonObject6 = new JSONObject();
//    jsonObject6.put("name", "tala");
//    jsonObject6.put("age", 20);
//    clientHandler.writeDocument("createIndexTest", "test1", jsonObject);
//    clientHandler.writeDocument("createIndexTest", "test1", jsonObject2);
//    clientHandler.writeDocument("createIndexTest", "test1", jsonObject3);
//    clientHandler.writeDocument("createIndexTest", "test1", jsonObject4);
//    clientHandler.writeDocument("createIndexTest", "test1", jsonObject5);
//    clientHandler.writeDocument("createIndexTest", "test1", jsonObject6);
//
    //        clientHandler.deleteCollection("films","dockerVideos");
    //        clientHandler.updateDocument("films","meta","films.meta.1",jsonObject);
  }
}
