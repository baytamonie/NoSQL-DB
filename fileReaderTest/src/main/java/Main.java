import jdk.nashorn.internal.runtime.JSONFunctions;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        RandomAccessFileTester randomAccessFileTester = new RandomAccessFileTester();
        File file = new File("/home/bayta/Desktop/Final project atypon/fileReaderTest/src/main/java/data.json");
        for (Byte b: randomAccessFileTester.readSomeDataFromFile(file,0,10)){
            System.out.print((char) b.byteValue());
        }
    }
}
