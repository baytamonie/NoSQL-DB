import documents.JSONConverter;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
//        Server server = new Server();
//        server.startNode();
//        Server server2 = new Server();
//        server2.startNode();
//        Server server3= new Server();
//        server3.startNode();
        String message = "Hello, world!";
        String filePath = "src/main/resources/dataWrite.txt";
        String dataPath = "src/main/resources/databases/database1/collection1/data.json";
        long filePointer1 = 0;
        long objectEnd1 = 96;
        long filePointer2 = 96;
        long objectEnd2 = 181;
        long filePointer3 = 181;
        long objectEnd3 = 257;
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

}
