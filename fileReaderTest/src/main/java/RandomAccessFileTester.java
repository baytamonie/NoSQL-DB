import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileTester {


    public byte[] readSomeDataFromFile(File file, int pos, int size) throws IOException {
        java.io.RandomAccessFile randomAccessFile = new java.io.RandomAccessFile(file,"r");
        randomAccessFile.seek(pos);
        byte[] bytesToRead = new byte[size];
        randomAccessFile.read();
        randomAccessFile.readFully(bytesToRead);
        randomAccessFile.close();
        return bytesToRead;
    }
    public void writeDataToFile(String file, String data, int pos) throws IOException{
        RandomAccessFile fileAF = new RandomAccessFile(file,"rw");
        fileAF.seek(pos);
        fileAF.write(data.getBytes());
        System.out.println(fileAF.getFilePointer());
        fileAF.close();
    }


}
