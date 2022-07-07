import driver.ClientDriver;
import org.json.simple.JSONObject;

public class WriteDocumentTest {

  static ClientDriver clientDriver = new ClientDriver();

  public static void main(String[] args) {
    clientDriver.createConnection();
    clientDriver.login("bayta","123");
    createDatabase();
    createCollection();
    writeDocument();
//    deleteDocument();
  }

  public static void createDatabase(){
    clientDriver.createDatabase("test");
  }
  public static  void writeDocument(){
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name","ahmad");
    clientDriver.writeDocument("test","testCollection",jsonObject);
  }
  public static void deleteDocument(){
    clientDriver.deleteDocument("test","testCollection","test.testCollection.0");
  }

  public static void createCollection(){
    JSONObject schema = new JSONObject();
    schema.put("name","string");
    clientDriver.createCollection("test","testCollection",schema);
  }


}
