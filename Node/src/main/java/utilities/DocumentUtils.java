package utilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public  class DocumentUtils {

    public static boolean checkIfDbExists(String dbName){
        if(dbName == null)
            return false;
        String path = "src/main/resources/databases/";
        path += dbName;
        File tempFile = new File(path);
        boolean exists = tempFile.exists();
        return exists;
    }
    public static boolean checkIfCollectionExists(String dbName, String collectionName){
        if(dbName == null || collectionName == null)
            return false;
        String path = "src/main/resources/databases/";
        path += dbName
                +'/'
                +collectionName;
        File tempFile = new File(path);
        boolean exists = tempFile.exists();
        return exists;
    }
    public static String pathBuilder(String dbName, String collectionName, String fileName){

        return "src/main/resources/databases/"
                +dbName
                +'/'
                +collectionName
                +'/'
                +fileName;
    }

    public static boolean checkIfPropertyInSchema(String propertyName, String path){
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
