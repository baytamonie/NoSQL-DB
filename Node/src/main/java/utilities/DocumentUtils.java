package utilities;

import java.io.File;

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

}
