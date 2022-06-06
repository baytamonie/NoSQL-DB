import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class RandomAccessFileTesting {

    private final RandomAccessFile randomAccessFile;
    private final String idsPath;

    public RandomAccessFileTesting() throws FileNotFoundException {
        idsPath = "src/main/resources/databases/database1/collection1/ids.json";
        randomAccessFile = new RandomAccessFile(idsPath,"rw");

    }

}
