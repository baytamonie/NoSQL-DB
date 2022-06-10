import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileTesting {

    @Test
    public void checkIfDbExists() throws IOException {
        String path = "src/main/resources/databases/";
        path = path + "database1";
        File tempFile = new File(path);
        boolean exists = tempFile.exists();
        System.out.println(exists);
    }

}
