import driver.ClientDriver;
import org.json.simple.JSONObject;

public class SchemaTest {
    static ClientDriver clientDriver = new ClientDriver();

    public static void main(String[] args) {
        clientDriver.createConnection();
        clientDriver.login("bayta","123");

        wrongSchema();
        rightSchema();

    }

    public static void wrongSchema(){
        JSONObject object = new JSONObject();
        object.put("age","22");
        clientDriver.writeDocument("videos","aws",object);
    }
    public static void rightSchema(){
        JSONObject object = new JSONObject();
        object.put("name","videoTest");
        object.put("url","videoTest");
        object.put("description","videoTest");
        clientDriver.writeDocument("videos","aws",object);
    }





}
