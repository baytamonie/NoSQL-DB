import driver.ClientDriver;
import org.json.simple.JSONObject;

public class Main {


    private final static ClientDriver driver = new ClientDriver();

    public static void main(String[] args){
        driver.createConnection();
        driver.login("bayta","123");
        createDatabaseTest();
        createCollectionTest();
        writeDocuemntTest();
        writeDocuemntTest();
        writeDocuemntTest();
        writeDocuemntTest();
        writeDocuemntTest();
        writeDocuemntTest();
        writeDocuemntTest();
        writeDocuemntTest();
        createIndexTest();
        while (true){

        }
    }

    private static void  createDatabaseTest(){
        String name = "test";
        driver.createDatabase(name);
    }

    private static void createCollectionTest(){
        String dbName = "test";
        String collectionName = "testCollection";
        JSONObject schema = new JSONObject();
        schema.put("name","string");
        driver.createCollection(dbName,collectionName,schema);
    }
    private static void writeDocuemntTest(){
        String dbName = "test";
        String collectionName = "testCollection";
        JSONObject object = new JSONObject();
        object.put("name","ahmad");
        driver.writeDocument(dbName,collectionName,object);
    }

    private static void createIndexTest(){
        driver.createIndex("test","testCollection","name");
    }








}
