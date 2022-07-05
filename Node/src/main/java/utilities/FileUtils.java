package utilities;

import documents.IdsObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;

public class FileUtils {

    private static volatile FileUtils fileUtils;

    private FileUtils() {}

    public static FileUtils getInstance() {
        FileUtils ref = fileUtils;
        if (ref == null) {
            synchronized (FileUtils.class) {
                ref = fileUtils;
                if (ref == null) fileUtils = ref = new FileUtils();
            }
        }
        return ref;
    }
    private  final JSONParser jsonParser = new JSONParser();

    public  JSONArray loadData(String path){
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

    public  boolean checkIfPropertyIsIndexed(String dbName,String collectionName, String propertyName){
        String path = "src/main/resources/databases/"+dbName+'/'+collectionName+'/'+propertyName+".json";
        File file = new File(path);
        return file.exists();
    }
    public  JSONObject getObjectRandomAccessFile(String path, long startIndex, long endIndex){
        int sizeOfBytes = (int)(endIndex - startIndex);
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(path,"r")){
            randomAccessFile.seek(startIndex);
            byte[] bytes = new byte[(int) sizeOfBytes];
            randomAccessFile.read(bytes);
            String s = new String(bytes);
            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            randomAccessFile.close();
            return jsonObject;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

    }
    public  HashMap<String,IdsObject> loadIdsJSON(String path){
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
    public  boolean checkIfFileOrDirectoryExists(String path) {
        if (path == null) return false;
        File file = new File(path);
        return file.exists();
    }
    public  boolean deleteDirectory(String path) {
        try {
            deleteDirectoryRecursionJava(new File(path));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

public  void makeDirectory(String path) {
        File file = new File(path);
        file.mkdir();

    }

    private  void deleteDirectoryRecursionJava(File file) throws IOException {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectoryRecursionJava(entry);
                }
            }
        }
        if (!file.delete()) {
            throw new IOException("Failed to delete " + file);
        }
    }

}
