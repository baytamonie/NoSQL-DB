import documents.entities.JSONSchema;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class SchemaCheck {

  @Test
  public void checkIfSchemaMatches() throws IOException, ParseException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "ahmad");
    jsonObject.put("age", 10);
    Iterator<String> objectKeys = jsonObject.keySet().iterator();
    String path = "src/main/resources/databases/TestDatabase/TestCollection/schema.json";
    JSONParser jsonParser = new JSONParser();
    FileReader fileReader = new FileReader(path);
    Object dataObject = jsonParser.parse(fileReader);
    JSONObject schema = (JSONObject) dataObject;
    Iterator<String> schemaKeys = schema.keySet().iterator();
    JSONSchema jsonSchema = new JSONSchema(schema);
    while (schemaKeys.hasNext()) {
      String key = schemaKeys.next();
      if (jsonObject.containsKey(key))
        System.out.println(jsonObject.get(key).getClass());


    }
  }
}
