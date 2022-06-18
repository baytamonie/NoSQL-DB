package utils;

import documents.entities.JSONSchema;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

public class DocumentsUtils {
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
    public static boolean checkIfSchemaMatches(String path,JSONObject jsonObject){
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
            JSONObject schema = (JSONObject) (jsonParser.parse(fileReader));
            Iterator<String> schemaKeys = schema.keySet().iterator();
            Iterator<String> jsonObjectKeys = schema.keySet().iterator();
            int numOfJsonObjectKeys = 0;
            jsonObject.remove("_id");
            while(jsonObjectKeys.hasNext()){
                jsonObjectKeys.next();
                numOfJsonObjectKeys++;
            }
            JSONSchema jsonSchema = new JSONSchema(schema);
            int numOfItemsInSchema =0;
            while (schemaKeys.hasNext()) {
                String key = schemaKeys.next();
                if(key=="_id")
                    continue;
                if(jsonObject.containsKey(key) && jsonObject.get(key).getClass().equals(jsonSchema.getPropertyType(key))){
                    numOfItemsInSchema++;
                }
                else
                    return false;
            }
            if(numOfItemsInSchema==numOfJsonObjectKeys)
            {
        System.out.println(numOfItemsInSchema);
                return true;
             }
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    public  static String  generateKey(String dbName,String collectionName){
        String key = dbName+'.'+collectionName+'.';
        String pointerPath = "src/main/resources/databases/idsPointer";
        try(FileInputStream fileInputStream = new FileInputStream(pointerPath)){
            Scanner scanner = new Scanner(fileInputStream, "UTF-8");
            String line = scanner.nextLine();
            writeNewKey(line);
            key = key + line;
            fileInputStream.close();
            scanner.close();
            return key;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }
    private static  void writeNewKey(String key){
        File file = new File("src/main/resources/databases/idsPointer");
        file.delete();
        try {
            Long keyValue = Long.valueOf(key);
            keyValue++;
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("src/main/resources/databases/idsPointer");
            fileWriter.write(String.valueOf(keyValue));
            System.out.println("new key: "+keyValue);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
