import java.io.*;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import documents.IdsDocument;
import documents.entities.DocumentEntity;
import org.apache.commons.lang3.SerializationUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import java.io.*;


import java.util.HashMap;

public class RandomAccessFileTesting  {

//    private HashMap<String, DocumentEntity> ids;
    @Test
    public void loadObjectFromRAF(){
    String idsDocPath = "src/main/resources/databases/database1/collection1/ids.json";
    String dataPath = "src/main/resources/databases/database1/collection1/data.json";
    try(RandomAccessFile randomAccessFile = new RandomAccessFile(dataPath,"rw")){
        try(FileReader fileReader = new FileReader(idsDocPath)){
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(fileReader);
            JSONArray idsLoc = (JSONArray) obj;
            HashMap<String, IdsDocument> ids = new HashMap<>();
            for(Object object : idsLoc){
                ids.put((String)((JSONObject) object).get("_id"),new IdsDocument((Long)((JSONObject)object).get("begin"),(Long)((JSONObject)object).get("end"),(String)((JSONObject)object).get("_id")));
            }
            IdsDocument document = ids.get("129124");
            long i = document.getBegin();
            long x = document.getEnd() -document.getBegin();
            int x1 = (int) x;
            byte[] bytes = new byte[x1];
            randomAccessFile.seek(i);
            int i1 = 0;
        System.out.println("hey");
        String s= "";
            while(i1<x1){
                bytes[i1] = randomAccessFile.readByte();
                System.out.print((char) bytes[i1]);
                s+=(char) bytes[i1];
                i1++;
            }

            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            System.out.println(jsonObject.get("name"));



        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    }
    @Test
    public void readRandomAccessFile(){
        String dataPath = "src/main/resources/databases/database1/collection1/data.json";
        long filePointer1 = 1;
        long objectEnd1 = 107;
        long filePointer2 = 108;
        long objectEnd2 = 205;
        long filePointer3 = 208;
        long objectEnd3 =291;
        boolean endOfFile = false;
        String s1="";
        String s2="";
        String s3="";
        try (RandomAccessFile randomFile = new RandomAccessFile(dataPath,"rw")){
            long i = 1;
            randomFile.seek(filePointer1);
            while(i!=objectEnd1){
                char c = (char)randomFile.readByte();
                s1+=c;
                i++;
            }
            System.out.println(s1);
            i = filePointer2;
            randomFile.seek(filePointer2);
//
            while (i != objectEnd2) {
                char c = (char)randomFile.readByte();
                s2+=c;
                i++;
            }
            System.out.println(s2);
            randomFile.seek(filePointer3);
            i=filePointer3;
            while(i!=objectEnd3){
                char c = (char)randomFile.readByte();
                s3+=c;
                i++;
            }
            System.out.println(s3);

        } catch (IOException e) {
            System.out.println(e.getClass());
        }

    }
    @Test
    public void loadIdsLoc() throws FileNotFoundException {
        String idsDocPath = "src/main/resources/databases/database1/collection1/ids.json";
        try(FileReader fileReader = new FileReader(idsDocPath)){
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(fileReader);
            JSONArray idsLoc = (JSONArray) obj;
            HashMap<String, IdsDocument> ids = new HashMap<>();
            for(Object object : idsLoc){
                ids.put((String)((JSONObject) object).get("_id"),new IdsDocument((Long)((JSONObject)object).get("begin"),(Long)((JSONObject)object).get("end"),(String)((JSONObject)object).get("_id")));
        System.out.println(ids.get((String)((JSONObject) object).get("_id")));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }



    public void loadSchema(){

    }
    public void readDocById(){

    }





}


