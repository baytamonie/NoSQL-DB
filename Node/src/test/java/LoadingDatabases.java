import documents.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

public class LoadingDatabases {

    @Test
    public void users(){

        List<User> users = new LinkedList<>();
        String idsDocPath = "src/main/resources/databases/usernames.json";
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(idsDocPath,"rw")) {
            try(FileReader fileReader = new FileReader(idsDocPath)){
                JSONParser jsonParser = new JSONParser();
                Object obj = jsonParser.parse(fileReader);
                JSONArray usersJSON = (JSONArray) obj;

                for(Object object : usersJSON){
                    JSONObject jsonObject  = (JSONObject) object;
                    User user = new User((String)jsonObject.get("username"),(String)jsonObject.get("password"),(String)jsonObject.get("permissions"));
                    users.add(user);
                }
                for(User user : users){
                    System.out.println(user.toString());
                }


        } catch (FileNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } ;
    } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
