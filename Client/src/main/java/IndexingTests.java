import driver.ClientDriver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class IndexingTests {

  public static void main(String[] args) {

    ClientDriver clientDriver = new ClientDriver();
    clientDriver.createConnection();
    clientDriver.login("bayta", "123");
    clientDriver.createIndex("videos", "aws", "name");
    JSONArray jsonArray = clientDriver.getProperties("videos", "aws", "name", "sleepy");
    for (Object obj : jsonArray) {
      System.out.println(obj);
    }
  }
}
