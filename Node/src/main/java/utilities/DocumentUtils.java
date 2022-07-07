package utilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public  class DocumentUtils {
    private static volatile DocumentUtils documentsUtils;
    public static DocumentUtils getInstance() {
        DocumentUtils ref = documentsUtils;
        if (ref == null) {
            synchronized (DocumentUtils.class) {
                ref = documentsUtils;
                if (ref == null) documentsUtils = ref = new DocumentUtils();
            }
        }
        return ref;
    }

    private DocumentUtils(){

    }
    public  boolean checkIfCollectionExists(String dbName, String collectionName){
        if(dbName == null || collectionName == null)
            return false;
        String path = "src/main/resources/databases/";
        path += dbName
                +'/'
                +collectionName;
        File tempFile = new File(path);
        return tempFile.exists();
    }
    public  String pathBuilder(String dbName, String collectionName, String fileName){

        return "src/main/resources/databases/"
                +dbName
                +'/'
                +collectionName
                +'/'
                +fileName;
    }

    public  boolean checkIfPropertyInSchema(String propertyName, String path){
        try(FileReader fileReader = new FileReader(path)){
            JSONParser jsonParser = new JSONParser();
            JSONObject schema = (JSONObject) (jsonParser.parse(fileReader));
            fileReader.close();
            return schema != null && schema.containsKey(propertyName);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

}
