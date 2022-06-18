package utilities;

import documents.IdsObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;

public class FileUtils {


    private static final JSONParser jsonParser = new JSONParser();

    public static JSONArray loadData(String path){
        Object dataObject = null;
        try(FileReader fileReader = new FileReader(path)){
             dataObject = jsonParser.parse(fileReader);
            JSONArray data = (JSONArray) dataObject;
            fileReader.close();
            return data;
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e){
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(dataObject);
            return jsonArray;
        }
    }
    public static JSONObject getObjectRandomAccessFile(String path, long startIndex, long endIndex){
        int sizeOfBytes = (int)(endIndex - startIndex);
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(path,"r")){
            randomAccessFile.seek(startIndex);
            byte[] bytes = new byte[(int) sizeOfBytes];
            randomAccessFile.read(bytes);
            String s = new String(bytes);
            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            randomAccessFile.close();
            return jsonObject;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
    public static HashMap<String,IdsObject> loadIdsJSON(String path){
        try(FileReader fileReader = new FileReader(path)){
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


}
