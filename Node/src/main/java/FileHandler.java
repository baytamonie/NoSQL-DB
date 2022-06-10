import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileHandler {


    private static final JSONParser jsonParser = new JSONParser();



    public static List<JSONObject> loadData(String path){
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(path,"rw")){
            FileReader fileReader = new FileReader(path);
            Object dataObject = jsonParser.parse(fileReader);
            JSONArray data = (JSONArray) dataObject;
            List<JSONObject> list = new LinkedList<>();
            for(Object obj : data){
                list.add((JSONObject) obj);
            }
            return list;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
