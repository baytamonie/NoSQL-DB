import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

public class GenerateKeys {
    @Test
    public  void  generateKey(){
        String dbName = "TestDatabase";
        String collectionName= "TestCollection";
        String key = dbName+'.'+collectionName+'.';
        String pointerPath = "src/main/resources/databases/idsPointer";
        try(FileInputStream fileInputStream = new FileInputStream(pointerPath)){
            Scanner scanner = new Scanner(fileInputStream, "UTF-8");
            String line = scanner.nextLine();
            writeNewKey(line);
            key = key + line;
      System.out.println(key);
            if(fileInputStream!=null)
                fileInputStream.close();
            if(scanner!=null)
                scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    public void writeNewKey(String key){
        File file = new File("src/main/resources/databases/idsPointer");
        file.delete();
        try {
            Long keyValue = Long.valueOf(key);
            keyValue++;
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("src/main/resources/databases/idsPointer");
            fileWriter.write(String.valueOf(keyValue));
            System.out.println("new key: "+keyValue);
            if(fileWriter!=null)
                fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
