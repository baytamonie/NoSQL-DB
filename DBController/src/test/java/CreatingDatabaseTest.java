import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreatingDatabaseTest {

    @Test
    public void createDb(){
        File file = new File("src/main/resources/databases/TestDatabase");
        file.mkdir();
    }
    @Test
    public void createCollection() throws IOException {
        File file = new File("src/main/resources/databases/TestDatabase/TestCollection");
        file.mkdir();
        File dataFile = new File("src/main/resources/databases/TestDatabase/TestCollection/data.json");
        dataFile.createNewFile();
        File schemaFile = new File("src/main/resources/databases/TestDatabase/TestCollection/schema.json");
        schemaFile.createNewFile();
        File idsFile = new File("src/main/resources/databases/TestDatabase/TestCollection/ids.json");
        idsFile.createNewFile();
    }

//    @Test
//    public void isFileDirectory(){
//        File db = new File("src/main/resources/databases/TestDatabase");
//        assertEquals(db.isDirectory(),true);
//        File db2 = new File("src/main/resources/databases/TestDatabase/");
//        assertEquals(db2.isDirectory(),true);
//        File collection = new File("src/main/resources/databases/TestDatabase/TestCollection");
//        assertEquals(collection.isDirectory(),true);
//        File collection2 = new File("src/main/resources/databases/TestDatabase/TestCollection/");
//        assertEquals(collection2.isDirectory(),true);
//        File dataFile = new File("src/main/resources/databases/TestDatabase/TestCollection/data.json");
//        assertEquals(dataFile.isDirectory(),false);
//
//    }
//    @Test
//    public void delete() throws IOException {
//        File file = new File("src/main/resources/databases/Employees/Managers");
//        deleteDirectoryRecursionJava(file);
//    }
    void deleteDirectoryRecursionJava(File file) throws IOException {
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



    @Test
    public void checkIfDbExists(){
        String databasesPath = "src/main/resources/databases";
        String databasesPath2 = "src/main/resources/databases/";
        String database1Path = "src/main/resources/databases/database1";
        String falsePath = "src/main/resources/databasess";
        String database1PathCollection1 = "src/main/resources/databases/database1/collection1";

        assertEquals(checkIfFileExists(databasesPath),true);
        assertEquals(checkIfFileExists(databasesPath2),true);
        assertEquals(checkIfFileExists(database1Path),true);
        assertEquals(checkIfFileExists(database1PathCollection1),true);
        assertEquals((checkIfFileExists(falsePath)),false);
    }

    public boolean checkIfFileExists(String path){
        File file = new File(path);
        return file.exists();
    }

}
