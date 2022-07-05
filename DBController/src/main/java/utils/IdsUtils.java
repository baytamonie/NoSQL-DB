package utils;

import documents.entities.IdsObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class IdsUtils {

    private static volatile IdsUtils idsUtils;
    private IdsUtils(){

    }
    public static IdsUtils getInstance() {
        IdsUtils ref = idsUtils;
        if (ref == null) {
            synchronized (IdsUtils.class) {
                ref = idsUtils;
                if (ref == null) idsUtils = ref = new IdsUtils();
            }
        }
        return ref;
    }

    public  HashMap<String, IdsObject> loadIdsJSON(String path){
        try(FileReader fileReader = new FileReader(path)){
            JSONParser jsonParser = new JSONParser();
            Object dataObject = jsonParser.parse(fileReader);
            JSONArray data = (JSONArray) dataObject;
            HashMap<String,IdsObject> list = new HashMap<>();
            for(Object obj : data){
                IdsObject idsObject = new IdsObject((Long)(((JSONObject)obj).get("begin")),(Long)(((JSONObject)obj).get("end")),(String)(((JSONObject)obj).get("_id")));
                list.put((String)(((JSONObject)obj).get("_id")),idsObject);
            }
            fileReader.close();
            return list;
        } catch (IOException | ParseException e) {
            return null;
        }
    }
    public  boolean deleteFromIds(String path,String id){
        try(FileReader fileReader = new FileReader(path)){
            JSONParser jsonParser = new JSONParser();
            Object dataObject = jsonParser.parse(fileReader);
            JSONArray data = (JSONArray) dataObject;
            JSONArray newData = new JSONArray();

            boolean isDocFound = false;

            for(Object obj : data){
                if(((JSONObject) obj).get("_id").equals(id)){
                    isDocFound = true;
                    continue;
                }
                newData.add(obj);
            }
            if(isDocFound){
                FileWriter fileWriter = new FileWriter(path);
                fileWriter.write(String.valueOf(newData));
                fileWriter.close();
                return true;
            }
            else{
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public  boolean updateIdsFile(String path, String key,long begin, long end){
        path = path+"/ids.json";
        File file = new File(path);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = new JSONArray();
            if(file.length()!=0)
            {
                jsonArray = (JSONArray) jsonParser.parse(fileReader);

            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("_id",key);
            jsonObject.put("begin",begin);
            jsonObject.put("end",end);
            jsonArray.add(jsonObject);
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(String.valueOf(jsonArray));
            fileWriter.close();

            return true;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


}
